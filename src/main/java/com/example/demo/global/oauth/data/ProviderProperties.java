package com.example.demo.global.oauth.data;

public record ProviderProperties(
        String clientId,
        String clientSecret,
        String tokenUri,
        String userInfoUri,
        String redirectUri
) {
}
