package com.example.demo.global.oauth.dto;

import com.example.demo.global.oauth.common.OAuthType;

public record OAuthUserResponse(
        String providerId,
        OAuthType provider,
        String email
) {
}
