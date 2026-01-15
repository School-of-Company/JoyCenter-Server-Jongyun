package com.example.demo.domain.auth.presentation.controller;

import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.domain.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;

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
}
