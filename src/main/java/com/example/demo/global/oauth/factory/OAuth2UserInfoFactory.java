package com.example.demo.global.oauth.factory;

import com.example.demo.global.oauth.OAuth2UserInfo;
import com.example.demo.global.oauth.data.GoogleOAuth2UserInfo;
import com.example.demo.global.oauth.data.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(
            String registrationId,
            String accessToken,
            Map<String, Object> attributes) throws IllegalAccessException {
        if("google".equals(registrationId)){
            return new GoogleOAuth2UserInfo(attributes);
        } else if("kakao".equals(registrationId)){
            return new KakaoOAuth2UserInfo(attributes);
        }
        throw new IllegalAccessException("지원하지 않는 OAuth2 제공자입니다." + registrationId);
    }
}
