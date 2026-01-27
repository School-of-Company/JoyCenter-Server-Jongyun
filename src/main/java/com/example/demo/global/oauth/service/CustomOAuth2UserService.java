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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User)
            throws OAuth2AuthenticationProcessingException, IllegalAccessException {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.toResponse().email();
        MemberEntity member = null;

        if (StringUtils.hasText(email)) {
            member = memberRepository.findByEmail(email).orElseGet(() -> registerNewMember(oAuth2UserInfo));
        } else {
            member = registerNewMember(oAuth2UserInfo);
        }

        return new OAuth2UserPrincipal(oAuth2UserInfo, member, oAuth2User.getAttributes());
    }

    private MemberEntity registerNewMember(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.toResponse().email();
        if (!StringUtils.hasText(email)) {
            email = "temp_" + System.currentTimeMillis() + "@example.com";
        }
        MemberEntity member = MemberEntity.builder()
                .email(email)
                .provider(oAuth2UserInfo.toResponse().provider())
                .providerId(oAuth2UserInfo.toResponse().providerId())
                .build();
        return memberRepository.saveAndFlush(member);
    }
}
