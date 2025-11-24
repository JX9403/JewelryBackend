package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.UserResponse;
import com.ndn.JewelryBackend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

@Transactional
public interface UserService {

     UserDetailsService userDetailsService();

     Page<UserResponse> getAllUsers(String email, Pageable pageable);

     void update(Long userId, User user);
}
