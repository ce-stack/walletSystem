package com.system.wallet.services;

import com.system.wallet.dto.request.auth.RegisterRequest;
import com.system.wallet.dto.response.auth.AuthResponse;
import com.system.wallet.exception.ResourceNotFoundException;
import com.system.wallet.models.User;
import com.system.wallet.repositories.AuthRepository;
import com.system.wallet.repositories.UserRepository;
import com.system.wallet.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthService implements AuthRepository {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AuthService(PasswordEncoder passwordEncoder,  UserRepository userRepository, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse createYourAccount(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("email already exist");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = new User(hashedPassword , request.getEmail(), request.getFull_name());
        userRepository.save(newUser);
        jwtUtils.generateJwtToken(newUser.getEmail());
        return new AuthResponse(jwtUtils.generateJwtToken(newUser.getEmail()), newUser.getEmail(), newUser.getName());
    }
}
