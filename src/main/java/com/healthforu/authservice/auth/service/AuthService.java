package com.healthforu.authservice.auth.service;

import com.healthforu.authservice.auth.dto.TokenDto;
import com.healthforu.authservice.user.dto.LoginRequest;

public interface AuthService {
    TokenDto login(LoginRequest loginRequest);
}
