package rs.ac.singidunum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.singidunum.repo.StudentRepository;
import rs.ac.singidunum.repo.AdminRepository;
import rs.ac.singidunum.entity.Admin;
import java.util.Optional;
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Prvo tražimo admina po email-u
            Optional<Admin> admin = adminRepository.findByEmail(username);
            if (admin.isPresent()) {
                return admin.get();
            }
            // Ako nije admin, tražimo studenta po broju indeksa
            return studentRepository.findByBrojIndeksa(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Ovo je implementacija 'AuthenticationProvider'-a koja nedostaje
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Kažemo mu kako da nađe korisnika
        authProvider.setPasswordEncoder(passwordEncoder()); // Kažemo mu kako da poredi lozinke
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Definišemo algoritam za hešovanje lozinki
        return new BCryptPasswordEncoder();
    }
}