package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.presentation.data.TokenResponse;

public interface RefreshTokenService {
    void save(Long userId, String refreshToken);
    TokenResponse reissueAccessToken(String refreshToken);
    void delete(Long userId);
}
