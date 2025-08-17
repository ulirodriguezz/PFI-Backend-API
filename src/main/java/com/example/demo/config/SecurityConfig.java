package com.example.demo.config;

import com.example.demo.types.UserRoleType;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register","/login","/movements/**").permitAll()
                        .requestMatchers("/items/**").hasAuthority(UserRoleType.ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfif = new CorsConfiguration();
        corsConfif.setAllowedOrigins(List.of("http://192.168.1.4:8081","http://localhost:8081","**"));
        corsConfif.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        corsConfif.setAllowedHeaders(List.of("Authorization","Content-type"));
        corsConfif.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfif);
        return source;
    }

}
