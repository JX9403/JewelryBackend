package com.ndn.JewelryBackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.repository.TokenRepository;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.impl.CustomOAuth2UserServiceImpl;
import com.ndn.JewelryBackend.service.impl.CustomOidcUserServiceImpl;
import com.ndn.JewelryBackend.service.JwtService;
import com.ndn.JewelryBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    private final UserService userService;
    private final JwtService jwtService;
    private final ApiPermissionConfig apiPermissionConfig;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2UserServiceImpl customOAuth2UserServiceImpl;
    private final CustomOidcUserServiceImpl customOidcUserServiceImpl;
    private final TokenRepository tokenRepository;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userService, tokenRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,PasswordEncoder passwordEncoder) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(apiPermissionConfig.getPermitAllPatterns()).permitAll()
                        .requestMatchers(HttpMethod.GET, apiPermissionConfig.getPermitAllGet()).permitAll()
                        .requestMatchers(HttpMethod.POST, apiPermissionConfig.getPermitAllPost()).permitAll()
                        .requestMatchers(HttpMethod.PUT, apiPermissionConfig.getPermitAllPut()).permitAll()
                        .requestMatchers(HttpMethod.DELETE, apiPermissionConfig.getPermitAllDelete()).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserServiceImpl) // cho provider không dùng OIDC
                                .oidcUserService(customOidcUserServiceImpl) // thêm dòng này để Google cũng vào đây
                        )
                        .successHandler((request, response, authentication) -> {
                            DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

                            String refresh_token = (String) oauthUser.getAttributes().get("refresh_token");
                            String access_token = (String) oauthUser.getAttributes().get("access_token");

                            JwtAuthenticationResponse jwtResponse = JwtAuthenticationResponse.builder()
                                    .accessToken(access_token)
                                    .refreshToken(refresh_token)
                                    .build();

                            response.setContentType("application/json");
                            new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
                        })
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                );
        ;
        return http.build();
    }



}
