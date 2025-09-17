package com.multigenesys.booking.service;

import com.multigenesys.booking.dto.AuthRequest;
import com.multigenesys.booking.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}

