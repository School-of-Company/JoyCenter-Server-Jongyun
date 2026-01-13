package com.example.demo.global.security.filter;

import com.example.demo.global.jwt.JwtTokenProvider;
import com.example.demo.global.jwt.TokenParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenParser tokenParser;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = tokenParser.resolveToken(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                if (jwtTokenProvider.isAccessToken(token)) {

                    Authentication authentication = tokenParser.parseAuthentication(token);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
            log.error("예외 발생 {}",e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return request.getMethod().equalsIgnoreCase("OPTIONS")
                || pathMatcher.match("/api/auth/**", uri)
                || pathMatcher.match("/api/oauth2/**", uri)
                || pathMatcher.match("/login/oauth2/**", uri);
    }
}

