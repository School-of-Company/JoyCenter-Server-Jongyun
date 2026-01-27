package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.presentation.data.OAuthLoginRequest;
import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;

public interface OAuthLoginService {
    TokenResponse execute(OAuthLoginRequest request) throws OAuth2AuthenticationProcessingException;
}