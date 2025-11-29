package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.*;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.AuthService;
import com.ndn.JewelryBackend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;

    private final LogoutHandler logoutHandler;

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
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutHandler.logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.builder() .code(200)
                .status(true)
                .message("Logged out successfully!")
                .data(null)
                .timestamp(new Date())
                .build());
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
    public ResponseEntity<ApiResponse> refresh(HttpServletRequest request) throws
            IOException {
        JwtAuthenticationResponse jwtResponse = authService.refreshToken(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Token refreshed successfully")
                .data(jwtResponse)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe(){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(authService.getMe())
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(null)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
