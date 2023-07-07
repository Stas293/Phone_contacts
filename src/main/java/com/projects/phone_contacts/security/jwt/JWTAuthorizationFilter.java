package com.projects.phone_contacts.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            String jwtToken = jwtUtils.getJwt(request);
            log.info("JWT token: {}", jwtToken);
            if (jwtUtils.checkJWTToken(jwtToken)) {
                log.info("JWT token is valid");
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                if (decodedJWT.getClaim("authorities") != null) {
                    log.info("JWT token has authorities");
                    Authentication authentication = jwtUtils.getAuthentication(decodedJWT);
                    log.info("Authentication: {}", authentication);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (JWTVerificationException e) {
            log.error("JWT token is invalid: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
