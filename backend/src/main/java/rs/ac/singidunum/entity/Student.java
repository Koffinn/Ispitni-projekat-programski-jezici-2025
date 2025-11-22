package rs.ac.singidunum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "student")
@NoArgsConstructor
@Getter
@Setter
public class Student implements UserDetails { // <-- 1. IMPLEMENTIRAMO UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false, unique = true)
    private String brojIndeksa;

    @Column(nullable = false)
    private String smer;

    @Column(nullable = false)
    private String lozinka;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Prijava> prijave;

    // ----- 2. IMPLEMENTACIJA METODA IZ UserDetails INTERFEJSA -----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Za sada, svaki student ima ulogu 'STUDENT'
        return List.of(new SimpleGrantedAuthority("STUDENT"));
    }

    @Override
    public String getPassword() {
        // Vraća lozinku studenta
        return this.lozinka;
    }

    @Override
    public String getUsername() {
        // Koristimo broj indeksa kao jedinstveno korisničko ime
        return this.brojIndeksa;
    }

    @Override
    public boolean isAccountNonExpired() {
        // U našem slučaju, nalozi ne ističu
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Nalozi se ne zaključavaju
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Kredencijali ne ističu
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Svi nalozi su uvek omogućeni
        return true;
    }
}