package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.presentation.data.TokenResponse;

public interface RefreshTokenService {
    TokenResponse login(Long userId, String email);
    TokenResponse reissueAccessToken(String refreshToken);
}
