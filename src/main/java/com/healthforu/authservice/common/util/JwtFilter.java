package com.healthforu.authservice.common.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Request Header에서 토큰 추출
        String token = resolveToken(request);

        // 토큰 유효성 검사
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            
            // Redis 블랙리스트 체크 (로그아웃된 토큰인지 확인)
            String isLogout = redisTemplate.opsForValue().get("blacklist:" + token);

            if (ObjectUtils.isEmpty(isLogout)) {
                // 블랙리스트에 없으면 정상적인 토큰이므로 인증 객체를 컨텍스트에 저장
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("로그아웃된 토큰으로 접근 시도 발견: {}", token);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Header에서 "Authorization: Bearer " 부분을 떼고 토큰값만 가져오는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}