package com.multigenesys.booking.config;

import com.multigenesys.booking.entity.Role;
import com.multigenesys.booking.entity.User;
import com.multigenesys.booking.entity.Resource;
import com.multigenesys.booking.repository.RoleRepository;
import com.multigenesys.booking.repository.UserRepository;
import com.multigenesys.booking.repository.ResourceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final ResourceRepository resourceRepo;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        if (roleRepo.findByName("ADMIN").isEmpty()) {
            roleRepo.save(Role.builder().name("ADMIN").build());
        }
        if (roleRepo.findByName("USER").isEmpty()) {
            roleRepo.save(Role.builder().name("USER").build());
        }

        // create admin user
        if (userRepo.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepo.findByName("ADMIN").get();
            userRepo.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .build());
        }

        // create normal user
        if (userRepo.findByUsername("user").isEmpty()) {
            Role userRole = roleRepo.findByName("USER").get();
            userRepo.save(User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .roles(Set.of(userRole))
                    .enabled(true)
                    .build());
        }

        // sample resources
        if (resourceRepo.count() == 0) {
            resourceRepo.save(Resource.builder().name("Meeting Room A").type("Room").capacity(6).description("Small meeting room").active(true).build());
            resourceRepo.save(Resource.builder().name("Projector 1").type("Equipment").capacity(1).description("HD Projector").active(true).build());
        }
    }
}

