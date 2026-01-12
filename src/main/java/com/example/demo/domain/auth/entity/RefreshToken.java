package com.example.demo.domain.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    private Long userId;

    private String token;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    public void updateToken(String token, Long ttl) {
        this.token = token;
        this.ttl = ttl;
    }
}