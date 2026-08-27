package com.example.Plazoleta.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Plazoleta/restaurantes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Plazoleta/restaurante/*/platos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Plazoleta/restaurante/*/propietario/*/validar").permitAll()

                        // Endpoints por rol
                        .requestMatchers(HttpMethod.POST, "/Plazoleta/restaurante").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/Plazoleta/plato").hasRole("PROPIETARIO")
                        .requestMatchers(HttpMethod.PUT, "/Plazoleta/plato/**").hasRole("PROPIETARIO")
                        .requestMatchers(HttpMethod.PATCH, "/Plazoleta/plato/*/estado").hasRole("PROPIETARIO")
                        .requestMatchers(HttpMethod.POST, "/Plazoleta/pedido").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/Plazoleta/pedidos").hasRole("EMPLEADO")
                        .requestMatchers(HttpMethod.PATCH, "/Plazoleta/pedido/*/asignar").hasRole("EMPLEADO")
                        .requestMatchers(HttpMethod.PATCH, "/Plazoleta/pedido/*/listo").hasRole("EMPLEADO")
                        .requestMatchers(HttpMethod.PATCH, "/Plazoleta/pedido/*/entregar").hasRole("EMPLEADO")
                        .requestMatchers(HttpMethod.PATCH, "/Plazoleta/pedido/*/cancelar").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/Plazoleta/pedido/*/trazabilidad").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
