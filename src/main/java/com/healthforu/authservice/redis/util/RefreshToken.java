package com.healthforu.authservice.redis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 604800) // 7일(초 단위)
public class RefreshToken {

    @Id
    private String loginId;

    @Indexed
    private String refreshToken;
}
