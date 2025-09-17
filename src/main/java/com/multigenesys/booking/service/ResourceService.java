package com.multigenesys.booking.service;


import com.multigenesys.booking.dto.ResourceDto;

import java.util.List;

public interface ResourceService {
    ResourceDto createResource(ResourceDto dto);
    ResourceDto updateResource(Long id, ResourceDto dto);
    void deleteResource(Long id);
    ResourceDto getResource(Long id);
    List<ResourceDto> getAllResources();
}

