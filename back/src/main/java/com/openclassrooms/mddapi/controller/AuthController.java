package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthSuccess;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
            String token = authService.register(request);
            return ResponseEntity.ok().body(new AuthSuccess(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthSuccess(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("disconnected !");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        UserResponse userResponse = authService.me(authentication);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody RegisterRequest request, Authentication authentication) {
        String token = authService.update(request, authentication);
        return ResponseEntity.ok().body(new AuthSuccess(token));
    }
}
