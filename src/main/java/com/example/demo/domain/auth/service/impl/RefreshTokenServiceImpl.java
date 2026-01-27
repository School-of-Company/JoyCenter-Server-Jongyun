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
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long REFRESH_TTL = 604800;

    @Override
    @Transactional
    public TokenResponse login(Long userId, String email) {

        String accessToken = jwtTokenProvider.generateAccessToken(userId, email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, email);

        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .ttl(REFRESH_TTL)
                .build();

        System.out.println("--- Redis 저장 시도 ---");
        refreshTokenRepository.save(tokenEntity);
        System.out.println("--- Redis 저장 완료! 현재 저장된 총 개수: " + refreshTokenRepository.count() + " ---");

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse reissueAccessToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String email = jwtTokenProvider.getUserEmail(refreshToken);

        RefreshToken stored = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!stored.getToken().equals(refreshToken)) {
            throw new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, email);

        return new TokenResponse(newAccessToken, refreshToken);
    }
}