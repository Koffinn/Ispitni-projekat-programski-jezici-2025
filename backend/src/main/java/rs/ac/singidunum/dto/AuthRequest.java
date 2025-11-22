package rs.ac.singidunum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    // Koristićemo broj indeksa kao korisničko ime
    private String brojIndeksa;
    private String lozinka;
}