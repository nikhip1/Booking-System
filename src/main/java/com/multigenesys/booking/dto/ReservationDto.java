package com.multigenesys.booking.dto;

import com.multigenesys.booking.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReservationDto {
    private Long id;
    private Long resourceId;
    private Long userId; // for ADMIN-created reservation; ignored for USER
    private ReservationStatus status;
    private BigDecimal price;
    private Instant startTime;
    private Instant endTime;
    private Instant createdAt;
    private Instant updatedAt;
}

