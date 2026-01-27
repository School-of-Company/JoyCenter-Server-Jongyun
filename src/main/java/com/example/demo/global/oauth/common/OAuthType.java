package com.example.demo.global.oauth.common;

import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;

import java.util.Arrays;

public enum OAuthType {
    GOOGLE,
    KAKAO;

    public static OAuthType from(String type) {
        try {
            return Arrays.stream(values())
                    .filter(t -> t.name().equalsIgnoreCase(type))
                    .findFirst()
                    .orElseThrow(()-> new OAuth2AuthenticationProcessingException("지원하지 않는 제공자입니다."));
        } catch (OAuth2AuthenticationProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

