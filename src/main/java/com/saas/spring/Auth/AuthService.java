package com.saas.spring.Auth;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.saas.spring.Token.Token;
import com.saas.spring.Token.TokenRepository;
import com.saas.spring.Token.dto.LoginRequest;
import com.saas.spring.Token.dto.TokenResponse;
import com.saas.spring.User.User;
import com.saas.spring.User.UserRepository;
import com.saas.spring.Utils.JwtUtil;
import com.saas.spring.exception.UserExceptions.UserNotFoundException;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jUtil;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jUtil, TokenRepository tokenRepository,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jUtil = jUtil;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }


    private Token toEntity(User user, String token){
        return Token.builder()
            .isExpired(false)
            .isRevoked(false)
            .token(token)
            .user(user).build();
    }


    public TokenResponse tokenResponse(LoginRequest request){

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.nombre(), request.password()));

        User a = userRepository.findByNombre(request.nombre()).orElseThrow(()-> new IllegalArgumentException("No se encontro el usuario"));
        String accessToken = jUtil.generateToken(a);
        String refreshToken = jUtil.generateToken(a);

        revokedAllUserTokens(a);
        saveUserToken(a, refreshToken);

        return new TokenResponse(accessToken, "Se ha autenticado con exito");
    }

    public TokenResponse tokenRefresh(String header){

        if (header == null || !header.startsWith("BEARER")) {
            throw new IllegalArgumentException("Token Invalido");
        }

        String refreshToken = header.substring(7);
        Long id = jUtil.extractUserId(refreshToken);

        if (id == null) {
            throw new IllegalArgumentException("Token Refresh Invalido");
        }

        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        if (!jUtil.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Token Invalido");
        }

        String accessToken = jUtil.generateToken(user);

        revokedAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new TokenResponse(accessToken, "Se ha refrescado el token con exito");

    }


    private void revokedAllUserTokens(User a){
        List<Token> valid = tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(a.getId());

        if (!valid.isEmpty()) {
            for(Token t : valid){
                t.setExpired(true);
                t.setRevoked(true);
            }

            tokenRepository.saveAll(valid);
        }
    }

    private void saveUserToken(User a, String token){
        Token t = toEntity(a, token);
        tokenRepository.save(t);
    }
}
