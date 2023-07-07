package com.course.springstart.config;

import com.course.springstart.database.entity.Role;
import com.course.springstart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf().disable()
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers("/login", "/users/registration", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/{\\d+}").hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                )
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())
                        )
                )
                .build();
    }

    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcIdToken idToken = userRequest.getIdToken();
                String email = idToken.getClaim("email");
                UserDetails userDetails = userService.loadUserByUsername(email);
                DefaultOidcUser defaultOidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
                Set<Method> userMethods = Set.of(UserDetails.class.getMethods());
                return (OidcUser) Proxy.newProxyInstance(
                        SecurityConfiguration.class.getClassLoader(),
                        new Class[]{UserDetails.class, OidcUser.class},
                        (proxy, method, args) -> {
                            if (userMethods.contains(method)) {
                                return method.invoke(userDetails, args);
                            }
                            return method.invoke(defaultOidcUser, args);
                });
            }
        };
    }
}
