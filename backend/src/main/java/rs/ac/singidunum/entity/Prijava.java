package rs.ac.singidunum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "prijava") // Ispravljeno ime tabele
@NoArgsConstructor
@Getter
@Setter
public class Prijava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime vremePrijave;

    @ManyToOne
    @JoinColumn(name = "id_student", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_ispit", nullable = false)
    private Ispit ispit;
}