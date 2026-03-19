package com.saas.spring.Config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saas.spring.Token.Token;
import com.saas.spring.Token.TokenRepository;
import com.saas.spring.User.User;
import com.saas.spring.User.UserRepository;
import com.saas.spring.Utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class JwtFilter  extends OncePerRequestFilter{

    private final JwtUtil jUtil;

    private final UserDetail userDetails;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);
        Long userId = jUtil.extractUserId(accessToken);

        if (userId == null || SecurityContextHolder.getContext().getAuthentication()!= null) {
            return;
        }

        Token token = tokenRepository.findByToken(accessToken).orElse(null);

        if (token ==null || token.isExpired() || token.isRevoked()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("No se encontro el usuario con ese: "+userId));
        
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isTokenValid = jUtil.isTokenValid(accessToken, user);
        
        if (!isTokenValid) {
            return;
        }

        UserDetails userDetails = this.userDetails.loadUserByUsername(user.getNombre());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
