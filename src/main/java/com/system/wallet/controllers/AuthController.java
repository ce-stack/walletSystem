package com.system.wallet.controllers;

import com.system.wallet.dto.request.auth.RegisterRequest;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.createYourAccount(registerRequest));
    }
}
