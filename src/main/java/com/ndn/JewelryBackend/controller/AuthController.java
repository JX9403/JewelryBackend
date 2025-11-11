package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.*;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

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
}
