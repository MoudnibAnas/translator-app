package org.mql.genai.darijaatranslator.service;

import org.mql.genai.darijaatranslator.config.JwtUtil;
import org.mql.genai.darijaatranslator.models.AuthRequest;
import org.mql.genai.darijaatranslator.models.AuthResponse;
import org.mql.genai.darijaatranslator.models.User;
import org.mql.genai.darijaatranslator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResponse register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new AuthResponse(null, "Username is already taken!", null);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return new AuthResponse(null, "Email is already taken!", null);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Charger UserDetails correctement
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, "User registered successfully!", savedUser);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Charger UserDetails correctement
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // Récupérer l'utilisateur de la base
            User user = userRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new AuthResponse(token, "Login successful!", user);

        } catch (Exception e) {
            return new AuthResponse(null, "Invalid username or password!", null);
        }
    }
}