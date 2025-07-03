package rs.ac.singidunum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.singidunum.entity.Ispit;
import rs.ac.singidunum.repo.IspitRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IspitService {

    private final IspitRepository repository;

    public List<Ispit> getAllIspiti() {
        return repository.findAll();
    }

    public Optional<Ispit> getIspitById(Integer id) {
        return repository.findById(id);
    }

    public Ispit createIspit(Ispit ispit) {
        return repository.save(ispit);
    }

    public Ispit updateIspit(Integer id, Ispit ispitDetails) {
        Ispit ispit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ispit not found with id: " + id));

        ispit.setPredmet(ispitDetails.getPredmet());
        ispit.setProfesor(ispitDetails.getProfesor());
        ispit.setDatum(ispitDetails.getDatum());
        ispit.setSmer(ispitDetails.getSmer());

        return repository.save(ispit);
    }

    public void deleteIspit(Integer id) {
        repository.deleteById(id);
    }
}