package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.Role;
import com.ndn.JewelryBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String firstName = oidcUser.getGivenName();
        String lastName = oidcUser.getFamilyName();

        // Kiểm tra user trong DB
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstname(firstName);
                    newUser.setLastname(lastName);
                    newUser.setRole(Role.USER);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    return userRepository.save(newUser);
                });

        // Tạo token
        String refresh_token = jwtService.generateRefreshToken(new HashMap<>(), user);
        String access_token = jwtService.generateToken(user);

        // Gắn thêm token vào attributes để successHandler dùng
        Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
        attributes.put("access_token", access_token);
        attributes.put("refresh_token", refresh_token);

        return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email" // optional: tên key để lấy username
        ) {
            @Override
            public Map<String, Object> getAttributes() {
                return attributes; // trả về map mới có token
            }
        };
    }
}
