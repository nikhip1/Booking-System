package com.multigenesys.booking.service.impl;

import com.multigenesys.booking.dto.AuthRequest;
import com.multigenesys.booking.dto.AuthResponse;
import com.multigenesys.booking.entity.User;
import com.multigenesys.booking.repository.UserRepository;
import com.multigenesys.booking.security.JwtTokenProvider;
import com.multigenesys.booking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        String token = jwtTokenProvider.createToken(user.getUsername(), roles, user.getId());
        return new AuthResponse(token, user.getUsername());
    }
}

