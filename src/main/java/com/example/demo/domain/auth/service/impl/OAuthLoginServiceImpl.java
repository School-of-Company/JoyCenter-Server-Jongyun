package com.example.demo.domain.auth.service.impl;

import com.example.demo.domain.auth.presentation.data.OAuthLoginRequest;
import com.example.demo.domain.auth.presentation.data.TokenResponse;
import com.example.demo.domain.auth.service.OAuthLoginService;
import com.example.demo.domain.auth.service.RefreshTokenService;
import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.config.OAuthProviderConfig;
import com.example.demo.global.oauth.data.ProviderProperties;
import com.example.demo.global.oauth.dto.OAuthUserResponse;

import com.example.demo.global.oauth.factory.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthLoginServiceImpl implements OAuthLoginService {

    private final WebClient webClient;
    private final MemberRepository memberRepository;
    private final OAuthProviderConfig providerConfig;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse execute(OAuthLoginRequest request) throws OAuth2AuthenticationProcessingException {
        OAuthType type = OAuthType.from(request.provider());
        String socialAccessToken = getAccessToken(type, request);
        Map<String, Object> attributes = getUserAttributes(type, socialAccessToken);

        OAuthUserResponse userInfo = extractUserInfo(type, attributes);

        MemberEntity member = memberRepository.findByEmail(userInfo.email())
                .orElseGet(() -> registerMember(userInfo));

        return refreshTokenService.login(member.getId(), member.getEmail());
    }

    private OAuthUserResponse extractUserInfo(OAuthType type, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        try {
            return OAuth2UserInfoFactory.getOAuth2UserInfo(type.name(), attributes).toResponse();
        } catch (Exception e) {
            throw new OAuth2AuthenticationProcessingException("Failed to extract user info");
        }
    }

    private String getAccessToken(OAuthType type, OAuthLoginRequest request) throws OAuth2AuthenticationProcessingException {
        ProviderProperties provider = providerConfig.get(type);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", request.code());
        formData.add("redirect_uri", request.redirectUri());
        formData.add("client_id", provider.clientId());
        formData.add("client_secret", provider.clientSecret());

        return webClient.post()
                .uri(provider.tokenUri())
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .flatMap(m -> Mono.error(new OAuth2AuthenticationProcessingException(m))))
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(10))
                .blockOptional()
                .map(m -> (String) m.get("access_token"))
                .orElseThrow(() -> new OAuth2AuthenticationProcessingException("Access token response is empty"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserAttributes(OAuthType type, String accessToken) throws OAuth2AuthenticationProcessingException {
        ProviderProperties props = providerConfig.get(type);

        return webClient.get()
                .uri(props.userInfoUri())
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }

    private MemberEntity registerMember(OAuthUserResponse userInfo) {
        return memberRepository.save(MemberEntity.builder()
                .email(userInfo.email())
                .provider(userInfo.provider())
                .providerId(userInfo.providerId())
                .build());
    }
}