package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.*;
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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request){
        User user = authService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.refreshToken(request);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || !authentication.isAuthenticated()){
//            return ResponseEntity.status(401).body("Unauthorized");
//        }
//        String email = authentication.getName();
//        userService.changePassword(email, request);
//        return ResponseEntity.ok("Password changed");
//    }

}
