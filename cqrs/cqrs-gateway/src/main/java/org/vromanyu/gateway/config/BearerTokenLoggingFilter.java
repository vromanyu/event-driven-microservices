package org.vromanyu.gateway.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Profile("prod")
public class BearerTokenLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(BearerTokenLoggingFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.info("authentication is null");
        } else {
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                logger.info("jwt token: {}", jwtAuthenticationToken.getToken().getTokenValue());
                logger.info("jwt token claims: {}", jwtAuthenticationToken.getToken().getClaims());
                logger.info("jwt token authorities: {}", jwtAuthenticationToken.getAuthorities());
            }
        }
        filterChain.doFilter(request, response);
    }
}
