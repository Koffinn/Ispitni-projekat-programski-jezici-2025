package rs.ac.singidunum.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import rs.ac.singidunum.entity.Admin;
import rs.ac.singidunum.entity.Student;
@Service
public class JwtService {

    // Generišite vaš tajni ključ (npr. online Base64 generatorom) i nalepite ga ovde
    private static final String SECRET_KEY = "NDM1MzgyN0EzRkEyNTZFNkUzRjI1NkU2QTM1MzgyN0EzRkEyNTZFNkUzRjI1NkU2";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Dodajemo uloge korisnika kao "claim" u token
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // ---- DODAJEMO OVAJ DEO ----
        // Proveravamo da li je korisnik student ili admin i dodajemo njegov ID
        if (userDetails instanceof rs.ac.singidunum.entity.Student) {
            extraClaims.put("userId", ((rs.ac.singidunum.entity.Student) userDetails).getId());
        } else if (userDetails instanceof rs.ac.singidunum.entity.Admin) {
            extraClaims.put("userId", ((rs.ac.singidunum.entity.Admin) userDetails).getId());
        }
        // ---- KRAJ DELA ZA DODAVANJE ----

        return buildToken(extraClaims, userDetails);
    }

    // Privatna metoda koja gradi token
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token važi 24 sata
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}