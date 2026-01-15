package com.example.demo.global.oauth.data;

import com.example.demo.global.oauth.common.OAuthType;

import java.util.Map;


public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    private Map<String,Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public OAuthType getProvider() {
        return OAuthType.GOOGLE;
    }


    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
