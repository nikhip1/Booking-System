package com.multigenesys.booking.repository;

import com.multigenesys.booking.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> { }

