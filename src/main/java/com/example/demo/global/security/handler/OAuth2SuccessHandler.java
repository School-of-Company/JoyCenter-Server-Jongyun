package com.example.demo.global.security.handler;

import com.example.demo.domain.auth.entity.RefreshToken;
import com.example.demo.domain.auth.repository.RefreshTokenRepository;
import com.example.demo.global.jwt.JwtTokenProvider;
import com.example.demo.global.oauth.service.OAuth2UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.oauth2.redirect-uri:http://localhost:8080/oauth/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        Long userId = principal.getMemberEntity().getId();
        String email = principal.getMemberEntity().getEmail();

        log.info("OAuth2 로그인 성공: userId={}, email={}", userId, email);

        String accessToken = jwtTokenProvider.generateAccessToken(userId, email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, email);

        saveRefreshToken(userId, refreshToken);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
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