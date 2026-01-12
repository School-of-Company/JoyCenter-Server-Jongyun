package com.example.demo.global.security.handler;

import com.example.demo.domain.auth.repository.RefreshTokenRepository;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.global.auth.MemberDetails;
import com.example.demo.global.jwt.JwtTokenProvider;
import com.example.demo.domain.auth.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.oauth2.redirect-uri:/}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException  {
        MemberDetails member = (MemberDetails) authentication.getPrincipal();
        Long userId = member.getUserId();
        String email = member.getMember().getEmail();

        log.info("OAuth2 로그인 성공: userId={}, email={}", userId, email);

        String accessToken = jwtTokenProvider.generateAccessToken(userId, email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, email);

    }
    private void saveRefreshToken(Long userId, String refreshToken) {

        long ttlInSeconds = 7 * 24 * 60 * 60;

        RefreshToken token = refreshTokenRepository.findById(userId)
                .orElse(RefreshToken.builder()
                        .userId(userId)
                        .token(refreshToken)
                        .ttl(ttlInSeconds)
                        .build());
        token.updateToken(refreshToken, ttlInSeconds);
        refreshTokenRepository.save(token);

        log.info("Refresh Token 저장 완료: userId={}", userId);
    }


}
