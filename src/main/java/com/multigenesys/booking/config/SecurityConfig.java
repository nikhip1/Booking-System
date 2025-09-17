package com.multigenesys.booking.config;

import com.multigenesys.booking.security.CustomUserDetailsService;
import com.multigenesys.booking.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(cs -> cs.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /****Option 1 ****/
            // .authorizeHttpRequests(auth -> auth
            //         .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
            //         .requestMatchers("/resources/**").hasAnyRole("ADMIN", "USER")
            //         .requestMatchers("/reservations/**").hasAnyRole("ADMIN", "USER")
            //         .anyRequest().authenticated()
            // )

             /****Option 2 ****/
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

            //     // RESOURCES
                .requestMatchers(HttpMethod.GET, "/resources/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/resources/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/resources/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/resources/**").hasRole("ADMIN")

            //     // RESERVATIONS
                .requestMatchers("/reservations/**").hasAnyRole("ADMIN", "USER")

            //     // everything else
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // if you later need AuthenticationManager for login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

