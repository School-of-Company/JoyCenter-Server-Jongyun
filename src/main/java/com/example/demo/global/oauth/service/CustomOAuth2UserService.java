package com.example.demo.global.oauth.service;

import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.data.OAuth2UserInfo;
import com.example.demo.global.oauth.factory.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(
                    e.getMessage(), e
            );
        }
    }

    private OAuth2User processOAuth2User(
            OAuth2UserRequest userRequest,
            OAuth2User oAuth2User) throws OAuth2AuthenticationProcessingException, IllegalAccessException {

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(
                        registrationId,
                        oAuth2User.getAttributes()
                );

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException(
                    "OAuth2 공급자에서 이메일을 찾을 수 없습니다."
            );
        }

        MemberEntity member = memberRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(() -> registerNewMember(oAuth2UserInfo));

        return new OAuth2UserPrincipal(oAuth2UserInfo, member);
    }

    private MemberEntity registerNewMember(OAuth2UserInfo oAuth2UserInfo) {
        MemberEntity member = MemberEntity.builder()
                .email(oAuth2UserInfo.getEmail())
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .build();
        return memberRepository.save(member);
    }
}