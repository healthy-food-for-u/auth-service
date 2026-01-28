package com.healthforu.authservice.auth.controller;

import com.healthforu.authservice.auth.dto.TokenDto;
import com.healthforu.authservice.auth.service.AuthService;
import com.healthforu.authservice.user.dto.LoginRequest;
import com.healthforu.authservice.user.dto.SignUpRequest;
import com.healthforu.authservice.user.dto.UserResponse;
import com.healthforu.authservice.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // "Bearer " 부분을 제거 후 실제 토큰 값만 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);

            authService.logout(accessToken);

            return ResponseEntity.ok("로그아웃 되었습니다.");
        }

        return ResponseEntity.badRequest().body("잘못된 요청입니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam("id") String loginId) {
        boolean exists = userService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
