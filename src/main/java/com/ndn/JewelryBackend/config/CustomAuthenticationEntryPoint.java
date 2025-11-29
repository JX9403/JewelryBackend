package com.ndn.JewelryBackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException,
            ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse body = ApiResponse.builder()
                .status(false)
                .code(403)
                .message("Invalid token")
                .data(null)
                .timestamp(new Date())
                .build();

        String json = objectMapper.writeValueAsString(body);

        response.getWriter().write(json);
    }
}
