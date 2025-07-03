package rs.ac.singidunum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.singidunum.dto.AuthRequest;
import rs.ac.singidunum.entity.Student;
import rs.ac.singidunum.repo.StudentRepository;
import rs.ac.singidunum.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String login(AuthRequest request) {
        // 1. AuthenticationManager proverava da li su kredencijali (broj indeksa/email i lozinka) ispravni.
        // Ako nisu, ovde će se desiti greška (Exception).
        // Ako jesu, vraća se 'Authentication' objekat.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getBrojIndeksa(), // Ovo je username (bilo da je email ili broj indeksa)
                        request.getLozinka()
                )
        );

        // 2. Nema potrebe da ponovo tražimo korisnika u bazi.
        // On se već nalazi unutar 'Authentication' objekta koji smo dobili.
        UserDetails user = (UserDetails) authentication.getPrincipal();

        // 3. Generišemo JWT token za uspešno autentifikovanog korisnika.
        return jwtService.generateToken(user);
    }

    public String register(Student student) {
        if (repository.findByBrojIndeksa(student.getBrojIndeksa()).isPresent()) {
            throw new RuntimeException("Student with this index number already exists.");
        }

        student.setLozinka(passwordEncoder.encode(student.getLozinka()));
        repository.save(student);
        return jwtService.generateToken(student);
    }
}