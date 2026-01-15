package com.example.demo.global.oauth.service;

import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.data.OAuth2UserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class OAuth2UserPrincipal implements OAuth2User {

    private final OAuth2UserInfo oAuth2UserInfo;
    private final MemberEntity memberEntity;
    private Map<String, Object> attributes = Map.of();

    public OAuth2UserPrincipal(OAuth2UserInfo oAuth2UserInfo, MemberEntity memberEntity) {
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.memberEntity = memberEntity;
    }
    public String getEmail() {
        return oAuth2UserInfo.getEmail();
    }
    public MemberEntity getMember() {
        return this.memberEntity;
    }

    public OAuthType getProvider() {
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

