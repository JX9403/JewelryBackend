package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.Role;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger log = LoggerFactory.getLogger(com.ndn.JewelryBackend.service.impl.CustomOAuth2UserServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        System.out.println("✅ CustomOAuth2UserService triggered!");

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        log.info(String.valueOf(email));
        // Kiểm tra user có tồn tại chưa
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setLastname(lastName);
                    newUser.setFirstname(firstName);
                    newUser.setRole(Role.USER);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    log.info(String.valueOf(newUser));
                    return userRepository.save(newUser);
                });

        // Tạo JWT token cho user
        String refresh_token = jwtService.generateRefreshToken(new HashMap<>(), user);

        String access_token = jwtService.generateToken(user);

        // Thêm token vào attributes để frontend nhận
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("access_token", access_token);
        attributes.put("refresh_token", refresh_token);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attributes,
                "email"
        ){
            @Override
            public Map<String, Object> getAttributes() {
                return attributes; // trả về map mới có token
            }
        };
    }
}

