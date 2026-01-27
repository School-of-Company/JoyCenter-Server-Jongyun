package com.example.demo.global.oauth.data;

import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.dto.OAuthUserResponse;

import java.util.Map;


public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    private Map<String,Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public OAuthUserResponse toResponse() {
        return new OAuthUserResponse(
                (String) attributes.get("sub"),
                OAuthType.GOOGLE,
                (String) attributes.get("email")
        );
    }
}
