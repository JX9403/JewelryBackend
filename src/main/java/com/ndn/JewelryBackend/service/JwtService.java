package com.ndn.JewelryBackend.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface JwtService {
    String extractUsername (String token);
    String generateToken(UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
    Boolean isTokenExpired(String token);
    String generateRefreshToken(Map<String, Objects> extraClaims, UserDetails userDetails);
}
