package com.example.demo.domain.auth.presentation.data;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public boolean isExpired() {
        return this.accessToken == null && this.refreshToken == null;
    }
}
