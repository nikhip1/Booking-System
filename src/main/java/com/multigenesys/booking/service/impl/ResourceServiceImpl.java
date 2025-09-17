package com.multigenesys.booking.service.impl;

import com.multigenesys.booking.dto.ResourceDto;
import com.multigenesys.booking.entity.Resource;
import com.multigenesys.booking.exception.ResourceNotFoundException;
import com.multigenesys.booking.repository.ResourceRepository;
import com.multigenesys.booking.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    public ResourceDto createResource(ResourceDto dto) {
        Resource resource = mapToEntity(dto);
        Resource saved = resourceRepository.save(resource);
        return mapToDto(saved);
    }

    @Override
    public ResourceDto updateResource(Long id, ResourceDto dto) {
        Resource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setDescription(dto.getDescription());
        existing.setCapacity(dto.getCapacity());
        existing.setActive(dto.isActive());

        Resource updated = resourceRepository.save(existing);
        return mapToDto(updated);
    }

    @Override
    public void deleteResource(Long id) {
        Resource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        resourceRepository.delete(existing);
    }

    @Override
    public ResourceDto getResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        return mapToDto(resource);
    }

    @Override
    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Mapping helpers
    private ResourceDto mapToDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setType(resource.getType());
        dto.setDescription(resource.getDescription());
        dto.setCapacity(resource.getCapacity());
        dto.setActive(resource.isActive());
        return dto;
    }

    private Resource mapToEntity(ResourceDto dto) {
        return Resource.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .capacity(dto.getCapacity())
                .active(dto.isActive())
                .build();
    }
}

