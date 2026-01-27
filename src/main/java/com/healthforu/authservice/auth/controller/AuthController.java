package com.healthforu.authservice.auth.controller;

import com.healthforu.authservice.auth.dto.TokenDto;
import com.healthforu.authservice.auth.service.AuthService;
import com.healthforu.authservice.user.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // "Bearer " 부분을 제거 후 실제 토큰 값만 추출합니다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);

            authService.logout(accessToken);

            return ResponseEntity.ok("로그아웃 되었습니다.");
        }

        return ResponseEntity.badRequest().body("잘못된 요청입니다.");
    }
}
