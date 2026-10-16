package com.cefet.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(cors -> Customizer.withDefaults())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET,  "/usuarios/existe").permitAll()
                .requestMatchers(HttpMethod.GET, "/arquivos/**").permitAll()

                .requestMatchers(HttpMethod.GET,   "/usuarios/meus-dados").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/usuarios/senha").authenticated()
                .requestMatchers(HttpMethod.POST,  "/uploads").authenticated()

                .requestMatchers(HttpMethod.GET,    "/professores").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/professores/**").hasAuthority("ADMINISTRADOR")

                .requestMatchers("/professores/**").hasAuthority("PROFESSOR")
                .requestMatchers("/questoes/**").hasAuthority("PROFESSOR")
                .requestMatchers("/atividades/**").hasAuthority("PROFESSOR")
                .requestMatchers("/categorias/**").hasAuthority("PROFESSOR")

                .anyRequest().authenticated())

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((req, res, e) ->
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN)))

            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}