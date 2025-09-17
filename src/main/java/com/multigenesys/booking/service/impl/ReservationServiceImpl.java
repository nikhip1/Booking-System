package com.multigenesys.booking.service.impl;

import com.multigenesys.booking.dto.ReservationDto;
import com.multigenesys.booking.entity.Reservation;
import com.multigenesys.booking.entity.Resource;
import com.multigenesys.booking.entity.User;
import com.multigenesys.booking.enums.ReservationStatus;
import com.multigenesys.booking.exception.ResourceNotFoundException;
import com.multigenesys.booking.exception.UnauthorizedException;
import com.multigenesys.booking.repository.ReservationRepository;
import com.multigenesys.booking.repository.ResourceRepository;
import com.multigenesys.booking.repository.UserRepository;
import com.multigenesys.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
// import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationDto createReservation(ReservationDto dto, String requesterUsername, boolean isAdmin) {
        // map DTO -> entity
        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + dto.getResourceId()));

        User user;
        if (isAdmin && dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            user = userRepository.findByUsername(requesterUsername).orElseThrow();
        }

        Reservation r = Reservation.builder()
                .resource(resource)
                .user(user)
                .status(dto.getStatus() == null ? ReservationStatus.PENDING : dto.getStatus())
                .price(dto.getPrice())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // optional: check overlapping CONFIRMED reservations if r.status == CONFIRMED
        if (r.getStatus() == ReservationStatus.CONFIRMED) {
            preventOverlap(r);
        }

        Reservation saved = reservationRepository.save(r);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public ReservationDto updateReservation(Long id, ReservationDto dto, String requesterUsername, boolean isAdmin) {
        Reservation ex = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        if (!isAdmin && !ex.getUser().getUsername().equals(requesterUsername)) {
            throw new UnauthorizedException("Not allowed");
        }

        if (dto.getStartTime() != null) ex.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) ex.setEndTime(dto.getEndTime());
        if (dto.getPrice() != null) ex.setPrice(dto.getPrice());
        if (dto.getStatus() != null) ex.setStatus(dto.getStatus());
        if (dto.getResourceId() != null) {
            Resource res = resourceRepository.findById(dto.getResourceId()).orElseThrow();
            ex.setResource(res);
        }
        ex.setUpdatedAt(Instant.now());
        if (ex.getStatus() == ReservationStatus.CONFIRMED) preventOverlap(ex);
        Reservation upd = reservationRepository.save(ex);
        return mapToDto(upd);
    }

    @Override
    public void deleteReservation(Long id, String requesterUsername, boolean isAdmin) {
        Reservation ex = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        if (!isAdmin && !ex.getUser().getUsername().equals(requesterUsername)) {
            throw new UnauthorizedException("Not allowed");
        }
        reservationRepository.delete(ex);
    }

    @Override
    public ReservationDto getReservation(Long id, String requesterUsername, boolean isAdmin) {
        Reservation ex = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        if (!isAdmin && !ex.getUser().getUsername().equals(requesterUsername)) {
            throw new UnauthorizedException("Not allowed");
        }
        return mapToDto(ex);
    }

    @Override
    public Page<ReservationDto> searchReservations(ReservationStatus status, BigDecimal minPrice, BigDecimal maxPrice,
                                                   String requesterUsername, boolean isAdmin, int page, int size, String sort) {

        Specification<Reservation> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        }
        if (minPrice != null || maxPrice != null) {
            spec = spec.and((root, q, cb) -> {
                if (minPrice != null && maxPrice != null) return cb.between(root.get("price"), minPrice, maxPrice);
                if (minPrice != null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            });
        }
        if (!isAdmin) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("user").get("username"), requesterUsername));
        }

        // sort param: field,dir
        Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String field = parts[0];
            Sort.Direction dir = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.DESC;
            s = Sort.by(dir, field);
        }

        Page<Reservation> pageResult = reservationRepository.findAll(spec, PageRequest.of(page, size, s));
        return pageResult.map(this::mapToDto);
    }

    // mapping
    private ReservationDto mapToDto(Reservation r) {
        ReservationDto dto = new ReservationDto();
        dto.setId(r.getId());
        dto.setResourceId(r.getResource().getId());
        dto.setUserId(r.getUser().getId());
        dto.setStatus(r.getStatus());
        dto.setPrice(r.getPrice());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        return dto;
    }

    // simple overlap check for CONFIRMED reservations
    private void preventOverlap(Reservation r) {
        Specification<Reservation> overlap = (root, q, cb) -> cb.and(
                cb.equal(root.get("resource").get("id"), r.getResource().getId()),
                cb.equal(root.get("status"), ReservationStatus.CONFIRMED),
                cb.lessThan(root.get("startTime"), r.getEndTime()),
                cb.greaterThan(root.get("endTime"), r.getStartTime())
        );
        long count = reservationRepository.count(overlap);
        if (count > 0) throw new RuntimeException("Overlapping confirmed reservation exists");
    }
}

