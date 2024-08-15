package au.gestionparcautomobile.aulsh.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    public JwtRequestFilter jwtRequestFilter(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        return new JwtRequestFilter(authenticationService, jwtUtil);
    }
}
