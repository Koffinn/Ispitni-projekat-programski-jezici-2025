package rs.ac.singidunum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.singidunum.entity.Prijava;

import java.util.List; // Dodajte ovaj import

@Repository
public interface PrijavaRepository extends JpaRepository<Prijava, Integer> {

    List<Prijava> findByStudentId(Integer studentId);
}