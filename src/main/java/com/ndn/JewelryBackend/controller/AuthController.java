package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.ChangePasswordRequest;
import com.ndn.JewelryBackend.dto.request.EmailConfirmationRequest;
import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.service.JwtService;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException e){
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request){
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String email = authentication.getName();
        userService.changePassword(email, request);
        return ResponseEntity.ok("Password changed");
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@Valid @RequestBody EmailConfirmationRequest request){
        try {
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ResponseEntity.ok("Email confirmed successfully");
        } catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Invalid confirmation code");
        } catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
