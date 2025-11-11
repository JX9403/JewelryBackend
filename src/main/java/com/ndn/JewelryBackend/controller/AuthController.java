package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.*;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.AuthService;
import com.ndn.JewelryBackend.service.JwtService;
import com.ndn.JewelryBackend.service.impl.JwtServiceImpl;
import com.ndn.JewelryBackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Login successfully!")
                .data(authService.login(loginRequest))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request){
        authService.registerUser(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Register successfully!")
                .data(null)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Register successfully!")
                .data(authService.refreshToken(request))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/login-success")
    public JwtAuthenticationResponse loginSuccess(OAuth2AuthenticationToken authentication) {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        // Lấy email từ OAuth2User
        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Tạo token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        return JwtAuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
