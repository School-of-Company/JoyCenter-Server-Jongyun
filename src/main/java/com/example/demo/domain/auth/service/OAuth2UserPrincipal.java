package com.example.demo.domain.auth.service;

import com.example.demo.global.oauth.OAuth2UserInfo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class OAuth2UserPrincipal implements OAuth2User {

    private final OAuth2UserInfo oAuth2UserInfo;
    private Map<String, Object> attributes = Map.of();

    public OAuth2UserPrincipal(OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
    }
    public String getEmail() {
        return oAuth2UserInfo.getEmail();
    }

    public String getProvider() {
        return oAuth2UserInfo.getProvider();
    }

    public String getProviderId() {
        return oAuth2UserInfo.getProviderId();
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }
}

