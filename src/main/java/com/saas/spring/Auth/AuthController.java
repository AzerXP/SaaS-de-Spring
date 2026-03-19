package com.saas.spring.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.spring.Token.dto.LoginRequest;
import com.saas.spring.Token.dto.TokenResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "Autenticacion", description = "Control de las autenticaciones y refresh token")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse t = authService.tokenResponse(loginRequest);
        return ResponseEntity.ok(t);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader (HttpHeaders.AUTHORIZATION) final String header) {
        TokenResponse t = authService.tokenRefresh(header);
        return ResponseEntity.ok(t);
    }
    
    

}
