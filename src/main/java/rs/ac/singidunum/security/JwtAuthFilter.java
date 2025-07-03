package rs.ac.singidunum.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Izdvajamo "Authorization" header iz zahteva
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail; // U našem slučaju, ovo će biti broj indeksa

        // 2. Proveravamo da li header postoji i da li počinje sa "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Ako ne, samo nastavljamo dalje bez provere tokena
            return;
        }

        // 3. Izdvajamo sam JWT token iz headera (preskačemo "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Izdvajamo korisničko ime (broj indeksa) iz tokena koristeći JwtService
        userEmail = jwtService.extractUsername(jwt);

        // 5. Proveravamo da li korisničko ime postoji i da li korisnik već nije autentifikovan
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Učitavamo podatke o korisniku iz baze koristeći UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Proveravamo da li je token validan
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Ako jeste, kreiramo token za autentifikaciju
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Kredencijali (lozinka) nam ne trebaju ovde
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 7. Postavljamo autentifikaciju u SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 8. Prosleđujemo zahtev sledećem filteru u nizu
        filterChain.doFilter(request, response);
    }
}