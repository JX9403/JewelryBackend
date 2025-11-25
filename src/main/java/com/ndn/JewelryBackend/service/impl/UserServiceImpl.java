package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.UserResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public Page<UserResponse> getAllUsers(String email, Pageable pageable) {
        if (email == null || email.isBlank()) {
            return userRepository.findAll(pageable).map(this::toResponse); // trả tất cả
        }
        return userRepository.findByEmailContainingIgnoreCaseOrderByCreatedAtDesc(email, pageable).map(this::toResponse);
    }

    @Override
    public void update(Long userId, User user) {

    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .build();
    }

}
