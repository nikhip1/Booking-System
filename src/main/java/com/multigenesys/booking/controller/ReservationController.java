package com.multigenesys.booking.controller;

import com.multigenesys.booking.dto.ReservationDto;
import com.multigenesys.booking.enums.ReservationStatus;
import com.multigenesys.booking.security.JwtPrincipal;
import com.multigenesys.booking.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationDto>> list(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            Authentication authentication) {

        var principal = (JwtPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Page<ReservationDto> result = reservationService.searchReservations(status, minPrice, maxPrice,
                principal.username(), isAdmin, page, size, sort);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable Long id, Authentication authentication) {
        var principal = (JwtPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(reservationService.getReservation(id, principal.username(), isAdmin));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody @Valid ReservationDto dto, Authentication authentication) {
        var principal = (JwtPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(reservationService.createReservation(dto, principal.username(), isAdmin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> update(@PathVariable Long id, @RequestBody ReservationDto dto, Authentication authentication) {
        var principal = (JwtPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(reservationService.updateReservation(id, dto, principal.username(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        var principal = (JwtPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        reservationService.deleteReservation(id, principal.username(), isAdmin);
        return ResponseEntity.noContent().build();
    }
}
