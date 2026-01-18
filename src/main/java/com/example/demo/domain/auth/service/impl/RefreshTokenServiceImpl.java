package com.example.demo.domain.auth.service.impl;

import com.example.demo.domain.auth.entity.RefreshToken;
import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.domain.auth.repository.RefreshTokenRepository;
import com.example.demo.domain.auth.service.RefreshTokenService;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;
import com.example.demo.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long REFRESH_TTL = 7 * 24 * 60 * 60;

    @Override
    public void save(Long userId, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(userId)
                .orElse(RefreshToken.builder()
                        .userId(userId)
                        .build());

        token.updateToken(refreshToken, REFRESH_TTL);
        refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public TokenResponse reissueAccessToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String email = jwtTokenProvider.getUserEmail(refreshToken);

        String newAccessToken =
                jwtTokenProvider.generateAccessToken(userId, email);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .build(); // refresh는 안 줌
    }

    @Override
    public void delete(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
