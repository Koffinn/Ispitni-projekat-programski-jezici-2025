package rs.ac.singidunum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ispit")
@NoArgsConstructor
@Getter
@Setter
public class Ispit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String predmet;

    @Column(nullable = false)
    private String profesor;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private String smer;

    @OneToMany(mappedBy = "ispit")
    @JsonIgnore
    private List<Prijava> prijave;
}