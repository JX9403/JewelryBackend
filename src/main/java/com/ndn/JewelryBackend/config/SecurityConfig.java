package com.ndn.JewelryBackend.config;

import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.JwtService;
import com.ndn.JewelryBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final ApiPermissionConfig apiPermissionConfig;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userService);
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
                        .requestMatchers("/api/auth/change-password").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(passwordEncoder))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
