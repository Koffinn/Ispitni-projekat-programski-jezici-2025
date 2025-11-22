package rs.ac.singidunum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.singidunum.dto.AuthRequest;
import rs.ac.singidunum.dto.AuthResponse;
import rs.ac.singidunum.entity.Student; // Potreban import za registraciju
import rs.ac.singidunum.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // --- NOVI ENDPOINT ZA REGISTRACIJU ---
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Student student) {
        String token = authService.register(student);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}