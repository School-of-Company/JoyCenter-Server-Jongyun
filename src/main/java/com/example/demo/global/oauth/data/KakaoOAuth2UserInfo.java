package com.example.demo.global.oauth.data;

import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.dto.OAuthUserResponse;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public OAuthUserResponse toResponse() {
        Map<String, Object> kakaoAccount =
                (Map<String, Object>) attributes.get("kakao_account");

        Map<String, Object> profile =
                kakaoAccount != null
                        ? (Map<String, Object>) kakaoAccount.get("profile")
                        : null;

        return new OAuthUserResponse(
                String.valueOf(attributes.get("id")),
                OAuthType.KAKAO,
                kakaoAccount != null ? (String) kakaoAccount.get("email") : null
        );
    }
}
