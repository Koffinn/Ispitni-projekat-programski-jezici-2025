package rs.ac.singidunum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.singidunum.entity.Student;

import java.util.Optional; // Uvezite Optional ako već nije

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByBrojIndeksa(String brojIndeksa);

}