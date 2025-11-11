package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RefreshTokenRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.entity.User;

public interface AuthService {
     void registerUser(RegisterRequest request) ;
     JwtAuthenticationResponse login(LoginRequest loginRequest);
     JwtAuthenticationResponse refreshToken(RefreshTokenRequest request);
}
