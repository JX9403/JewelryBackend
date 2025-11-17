package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.ChangePasswordRequest;
import com.ndn.JewelryBackend.dto.request.LoginRequest;
import com.ndn.JewelryBackend.dto.request.RefreshTokenRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.dto.response.JwtAuthenticationResponse;
import com.ndn.JewelryBackend.dto.response.UserResponse;
import com.ndn.JewelryBackend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface AuthService {
     void registerUser(RegisterRequest request) ;
     JwtAuthenticationResponse login(LoginRequest loginRequest);
     void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
     UserResponse getMe();
     void changePassword(ChangePasswordRequest request);
}
