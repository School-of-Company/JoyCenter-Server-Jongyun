package com.example.demo.domain.auth.presentation.data;

public record OAuthLoginRequest(
        String code,
        String provider
) {}
