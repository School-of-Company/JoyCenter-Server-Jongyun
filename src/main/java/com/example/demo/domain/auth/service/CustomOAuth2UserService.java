package com.example.demo.domain.auth.service;

import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.OAuth2UserInfo;
import com.example.demo.global.oauth.factory.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try{
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest,OAuth2User oAuth2User) throws IllegalAccessException, OAuth2AuthenticationProcessingException {

        String registrationId = oAuth2UserRequest.getClientRegistration()
                .getRegistrationId();

        String accessToken = oAuth2UserRequest.getAccessToken()
                .getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                accessToken,
                oAuth2User.getAttributes());

        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("OAuth2 공급자에서 이메일을 찾을 수 없습니다.");
        }

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }
}
