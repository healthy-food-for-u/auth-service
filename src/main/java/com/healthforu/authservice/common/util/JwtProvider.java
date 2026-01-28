package com.healthforu.authservice.common.util;

import com.healthforu.authservice.auth.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value; // 반드시 이걸로 import!
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final String secretKeyStr;
    private final long expireTime;
    private SecretKey key; // 주입받은 문자열을 Key 객체로 변환해서 보관

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.token.expire-time}") long expireTime) {
        this.secretKeyStr = secretKey;
        this.expireTime = expireTime;
    }

    @PostConstruct
    protected void init() {
        // 비밀키를 HS256 알고리즘에 적합한 Key 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String generateToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getId();

        Date now = new Date();

        return Jwts.builder()
                .setSubject(userId) // 여기에 ObjectId를 넣어야 Gateway가 이걸 읽어서 넘깁니다!
                .claim("auth", "ROLE_USER")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expireTime))
                .signWith(key) // 최신 방식
                .compact();

    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        Date now = new Date();
        // 일주일
        long refreshTokenExpireTime = 7 * 24 * 60 * 60 * 1000L;

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpireTime))
                .signWith(key)
                .compact();
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String authHeader = claims.get("auth").toString();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(authHeader)
        );
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 만료 시간 추출
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getExpiration();

        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String getLoginId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}