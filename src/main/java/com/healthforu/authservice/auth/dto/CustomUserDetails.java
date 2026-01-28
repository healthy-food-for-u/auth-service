package com.healthforu.authservice.auth.dto;

import com.healthforu.authservice.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final String id;
    private final String userName;

    public CustomUserDetails(User user) {
        super(user.getLoginId(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = user.getMongoId().toHexString();
        this.userName = user.getUserName();
    }
}
