package com.example.demo.domain.auth.presentation.controller;

import com.example.demo.domain.auth.presentation.data.OAuthLoginRequest;
import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.domain.auth.service.OAuthLoginService;
import com.example.demo.domain.auth.service.RefreshTokenService;
import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final OAuthLoginService oAuthLoginService;

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    headers = @Header(
                            name = "Authorization",
                            description = "Bearer {accessToken}",
                            schema = @Schema(type = "string")
                    ),
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token"),
            @ApiResponse(responseCode = "403", description = "만료된 Refresh Token")
    })
    @PatchMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @Parameter(description = "Refresh Token", required = true)
            @RequestHeader("RefreshToken") String refreshToken,
            HttpServletResponse response) {
        TokenResponse tokenResponse =
                refreshTokenService.reissueAccessToken(refreshToken);
        response.setHeader(
                "Authorization",
                "Bearer " + tokenResponse.accessToken()
        );

        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "OAuth 로그인", description = "OAuth 인증 코드로 로그인하여 토큰을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OAuth 로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 OAuth 요청"),
            @ApiResponse(responseCode = "401", description = "OAuth 인증 실패")
    })
    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> oauth(
            @RequestBody OAuthLoginRequest request) throws OAuth2AuthenticationProcessingException {
        TokenResponse response = oAuthLoginService.execute(request);
        return ResponseEntity.ok(response);
    }
}