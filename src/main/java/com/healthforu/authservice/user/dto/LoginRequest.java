package com.healthforu.authservice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;

    public LoginRequest(String loginId, String password){
        this.loginId=loginId;
        this.password=password;
    }
}
