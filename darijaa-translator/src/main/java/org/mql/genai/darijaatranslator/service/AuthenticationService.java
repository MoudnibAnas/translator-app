package org.mql.genai.darijaatranslator.service;

import org.mql.genai.darijaatranslator.models.AuthRequest;
import org.mql.genai.darijaatranslator.models.AuthResponse;
import org.mql.genai.darijaatranslator.models.User;
import org.mql.genai.darijaatranslator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new AuthResponse(null, "Username is already taken!", null);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return new AuthResponse(null, "Email is already taken!", null);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser);
        return new AuthResponse(token, "User registered successfully!", savedUser);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user != null) {
                String token = jwtUtil.generateToken(user);
                return new AuthResponse(token, "Login successful!", user);
            }

            return new AuthResponse(null, "Authentication failed!", null);

        } catch (Exception e) {
            return new AuthResponse(null, "Invalid username or password!", null);
        }
    }
}