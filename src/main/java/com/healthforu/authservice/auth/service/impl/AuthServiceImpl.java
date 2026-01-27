package com.healthforu.authservice.auth.service.impl;

import com.healthforu.authservice.auth.dto.TokenDto;
import com.healthforu.authservice.auth.service.AuthService;
import com.healthforu.authservice.common.util.JwtProvider;
import com.healthforu.authservice.redis.repository.RefreshTokenRepository;
import com.healthforu.authservice.redis.util.RefreshToken;
import com.healthforu.authservice.user.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public TokenDto login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = jwtProvider.generateToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        refreshTokenRepository.save(new RefreshToken(authentication.getName(), refreshToken));

        return TokenDto.from(accessToken, refreshToken);
    }
}
