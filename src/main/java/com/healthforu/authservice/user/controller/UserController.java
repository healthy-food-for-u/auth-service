package com.healthforu.authservice.user.controller;


import com.healthforu.authservice.user.dto.LoginRequest;
import com.healthforu.authservice.user.dto.SignUpRequest;
import com.healthforu.authservice.user.dto.UserResponse;
import com.healthforu.authservice.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        // JwtFilter가 블랙리스트 검증 후 SecurityContext에 담아준 인증 객체
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 토큰 생성 시 넣었던 loginId(Subject) 반환
        String loginId = authentication.getName();

        return ResponseEntity.ok(userService.getUserByLoginId(loginId));
    }


}
