package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ChangePasswordRequest;
import com.ndn.JewelryBackend.dto.request.RegisterRequest;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.enums.Role;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.EmailService;
import com.ndn.JewelryBackend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final EmailService emailService;


public UserDetailsService userDetailsService(){
    return new UserDetailsService() {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userRepository.findByEmail(username)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        }
    };
}

//public User getUserByEmail(String email){
//    return userRepository.findByEmail(email)
//            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//}

//public void changePassword(String email, ChangePasswordRequest request){
//    User user = getUserByEmail(email);
//    if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
//        throw new BadCredentialsException("Current password is incorrect");
//    }
//
//    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//    userRepository.save(user);
//}


}
