package com.example.demo.global.oauth.data;


import com.example.demo.global.oauth.common.OAuthType;

public interface OAuth2UserInfo {
    String getProviderId();
    OAuthType getProvider();
    String getEmail();
    String getName();
}
