package com.multigenesys.booking.service;

import com.multigenesys.booking.dto.ReservationDto;
import com.multigenesys.booking.enums.ReservationStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto dto, String requesterUsername, boolean isAdmin);
    ReservationDto updateReservation(Long id, ReservationDto dto, String requesterUsername, boolean isAdmin);
    void deleteReservation(Long id, String requesterUsername, boolean isAdmin);
    ReservationDto getReservation(Long id, String requesterUsername, boolean isAdmin);
    Page<ReservationDto> searchReservations(ReservationStatus status,
                                            BigDecimal minPrice, 
                                            BigDecimal maxPrice,
                                            String requesterUsername, 
                                            boolean isAdmin, 
                                            int page, 
                                            int size, 
                                            String sort);
}
