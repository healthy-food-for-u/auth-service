package com.healthforu.authservice.user.service;


import com.healthforu.authservice.user.dto.LoginRequest;
import com.healthforu.authservice.user.dto.SignUpRequest;
import com.healthforu.authservice.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponse signUp(SignUpRequest request);

    UserResponse login(LoginRequest request, HttpServletRequest httpServletRequest);

    void logout(HttpServletRequest request);

    boolean checkLoginIdDuplicate(String loginId);

    UserResponse getUserByLoginId(String loginId);
}
