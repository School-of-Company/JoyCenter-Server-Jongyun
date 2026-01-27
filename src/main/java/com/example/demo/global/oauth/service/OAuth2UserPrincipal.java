package com.example.demo.global.oauth.service;

import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.global.oauth.data.OAuth2UserInfo;
import com.example.demo.global.oauth.dto.OAuthUserResponse;
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
    private final MemberEntity memberEntity;
    private final Map<String, Object> attributes;

    public OAuth2UserPrincipal(OAuth2UserInfo oAuth2UserInfo, MemberEntity member, Map<String, Object> attributes) {
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.memberEntity = member;
        this.attributes = attributes;
    }

    public OAuthUserResponse toResponse() {
        return oAuth2UserInfo.toResponse();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return toResponse().providerId();
    }
}
