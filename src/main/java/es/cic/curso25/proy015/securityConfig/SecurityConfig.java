package es.cic.curso25.proy015.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // evita 403 en POST/PUT/DELETE
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // TODO abierto
                );
        return http.build();
    }
}
