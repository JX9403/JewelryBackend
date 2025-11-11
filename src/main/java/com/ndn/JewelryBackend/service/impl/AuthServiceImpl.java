package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.config.JwtAuthenticationFilter;
import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RefreshTokenRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.Role;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.AuthService;
import com.ndn.JewelryBackend.service.JwtService;
import com.ndn.JewelryBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    public void registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setSecondname(request.getSecondname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Valid email or password"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request){
        String email = jwtService.extractUsername(request.getToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if(jwtService.isTokenValid(request.getToken(), user)){
            var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(request.getToken());
        }

        return null;
    }
}
