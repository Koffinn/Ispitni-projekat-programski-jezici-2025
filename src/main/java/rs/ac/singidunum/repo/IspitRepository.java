package rs.ac.singidunum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.singidunum.entity.Ispit;

@Repository
public interface IspitRepository extends JpaRepository<Ispit, Integer> {
}