package com.ndn.JewelryBackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RefreshTokenRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.Role;
import com.ndn.JewelryBackend.enums.TokenType;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.AuthService;
import com.ndn.JewelryBackend.service.JwtService;
import com.ndn.JewelryBackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import com.ndn.JewelryBackend.repository.TokenRepository;
import com.ndn.JewelryBackend.entity.Token;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    public void registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Valid email or password"));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);

        return JwtAuthenticationResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(jwt).build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String refreshToken = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            refreshToken = authorizationHeader.substring(7);
            username = jwtService.extractUsername(refreshToken);
        }
        if(username != null){
            var   user = userRepository.findByEmail(username).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
               var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                var token = Token.builder()
                        .user(user)
                        .token(accessToken)
                        .tokenType(TokenType.BEARER)
                        .revoked(false)
                        .expired(false)
                        .build();

                tokenRepository.save(token);
               var jwtResponse = JwtAuthenticationResponse.builder()
                       .accessToken(accessToken)
                       .refreshToken(refreshToken)
                       .build();
               new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

}
