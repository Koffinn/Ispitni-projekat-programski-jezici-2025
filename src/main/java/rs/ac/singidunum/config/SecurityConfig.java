package rs.ac.singidunum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.ac.singidunum.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Onemogućavamo CSRF (Cross-Site Request Forgery) zaštitu,
                // jer koristimo JWT koji je po prirodi otporan на ову врсту напада.
                .csrf(AbstractHttpConfigurer::disable)

                // Definišemo pravila autorizacije za HTTP zahteve
                .authorizeHttpRequests(auth -> auth
                        // Dozvoljavamo slobodan pristup svim putanjama koje počinju sa /api/auth/
                        // Ovo je neophodno za login i registraciju.
                        .requestMatchers("/api/auth/**").permitAll()

                        // Samo korisnik sa ulogom "ADMIN" može da upravlja studentima.
                        .requestMatchers("/api/student/**").hasAuthority("ADMIN")

                        // Svi ulogovani korisnici mogu da vide ispite (GET),
                        // ali samo ADMIN može da ih kreira, menja ili briše.
                        .requestMatchers(HttpMethod.GET, "/api/ispit", "/api/ispit/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/ispit").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/ispit/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ispit/**").hasAuthority("ADMIN")

                        // Samo korisnik sa ulogom "STUDENT" može da prijavi ispit.
                        .requestMatchers("/api/prijava/**").hasAuthority("STUDENT")

                        // Svi ostali zahtevi koji nisu navedeni iznad moraju biti autentifikovani.
                        .anyRequest().authenticated()
                )

                // Definišemo da nećemo koristiti sesije, jer je svaki zahtev nezavisan (stateless).
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definišemo koji AuthenticationProvider da se koristi za proveru korisnika.
                .authenticationProvider(authenticationProvider)

                // Dodajemo naš JWT filter da se izvršava pre standardnog Spring filtera.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}