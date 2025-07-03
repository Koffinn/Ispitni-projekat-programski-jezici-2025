package rs.ac.singidunum.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.singidunum.entity.Admin;
import java.util.Optional;
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
}