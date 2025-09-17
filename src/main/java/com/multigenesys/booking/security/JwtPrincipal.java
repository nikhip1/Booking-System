package com.multigenesys.booking.security;

public record JwtPrincipal(String username, Long userId) { }
