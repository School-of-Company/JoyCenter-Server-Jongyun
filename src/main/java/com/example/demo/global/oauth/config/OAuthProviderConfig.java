package com.example.demo.global.oauth.config;

import com.example.demo.global.exception.oauth.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.common.OAuthType;
import com.example.demo.global.oauth.data.ProviderProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuthProviderConfig {
    private final Map<OAuthType, ProviderProperties> providers;

    public OAuthProviderConfig(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String googleClientId,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}") String googleClientSecret,
            @Value("${spring.security.oauth2.client.provider.google.token-uri}") String googleTokenUri,
            @Value("${spring.security.oauth2.client.provider.google.user-info-uri}") String googleUserInfoUri,
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret,
            @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String kakaoTokenUri,
            @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String kakaoUserInfoUri) {

        providers = Map.of(
                OAuthType.GOOGLE, new ProviderProperties(
                        googleClientId, googleClientSecret, googleTokenUri, googleUserInfoUri
                ),
                OAuthType.KAKAO, new ProviderProperties(
                        kakaoClientId, kakaoClientSecret, kakaoTokenUri, kakaoUserInfoUri
                )
        );
    }

    public ProviderProperties get(OAuthType provider) throws OAuth2AuthenticationProcessingException {
        ProviderProperties props = providers.get(provider);
        if (props == null) {
            throw new OAuth2AuthenticationProcessingException("지원하지 않는 OAuth 제공자입니다: " + provider);
        }
        return props;
    }
}
