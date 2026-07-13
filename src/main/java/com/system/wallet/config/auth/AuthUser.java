package com.system.wallet.config.auth;

import com.system.wallet.models.User;
import com.system.wallet.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUser {

    private UserRepository userRepository;

    public AuthUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new RuntimeException("Authentication required");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
