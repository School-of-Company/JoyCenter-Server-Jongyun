package com.example.demo.domain.auth.presentation.controller;

import com.example.demo.domain.auth.presentation.data.OAuthLoginRequest;
import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.domain.auth.service.OAuthLoginService;
import com.example.demo.domain.auth.service.RefreshTokenService;
import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final OAuthLoginService oAuthLoginService;

    @PatchMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @RequestHeader("RefreshToken")String refreshToken,
            HttpServletResponse response) {
        TokenResponse tokenResponse =
                refreshTokenService.reissueAccessToken(refreshToken);
        response.setHeader(
                "Authorization",
                "Bearer " + tokenResponse.accessToken()
        );

        return ResponseEntity.ok(tokenResponse);
    }
    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> oauth(@RequestBody OAuthLoginRequest request) throws OAuth2AuthenticationProcessingException {
        TokenResponse response = oAuthLoginService.execute(request);
        return ResponseEntity.ok(response);
    }
}
