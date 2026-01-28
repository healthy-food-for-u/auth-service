package com.healthforu.authservice.auth.service.impl;

import com.healthforu.authservice.auth.dto.CustomUserDetails;
import com.healthforu.authservice.user.domain.User;
import com.healthforu.authservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("사용자 없음");
        }

        return userRepository.findByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId));
    }

    private UserDetails createUserDetails(User user) {
        return new CustomUserDetails(user);
    }
}

