package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.ChangePasswordRequest;
import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RefreshTokenRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthService {
     void registerUser(RegisterRequest request) ;
     JwtAuthenticationResponse login(LoginRequest loginRequest);
     JwtAuthenticationResponse refreshToken(HttpServletRequest request) throws IOException;

     UserResponse getMe();
     void changePassword(ChangePasswordRequest request);
}
