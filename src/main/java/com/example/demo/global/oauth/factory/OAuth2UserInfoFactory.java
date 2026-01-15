package com.example.demo.global.oauth.factory;

import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.data.OAuth2UserInfo;
import com.example.demo.global.oauth.data.GoogleOAuth2UserInfo;
import com.example.demo.global.oauth.data.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(
            String registrationId,
            Map<String, Object> attributes) throws IllegalAccessException {
        OAuthType oAuthType = OAuthType.valueOf(registrationId.toUpperCase());
        return switch (oAuthType) {
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            default -> throw new IllegalAccessException("지원하지 않는 OAuth2 제공자입니다.");
        };
        }
    }
