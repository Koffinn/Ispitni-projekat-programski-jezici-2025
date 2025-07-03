package rs.ac.singidunum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.singidunum.entity.Ispit;
import rs.ac.singidunum.entity.Prijava;
import rs.ac.singidunum.entity.Student;
import rs.ac.singidunum.repo.IspitRepository;
import rs.ac.singidunum.repo.PrijavaRepository;
import rs.ac.singidunum.repo.StudentRepository;
import java.time.LocalDateTime;
import java.util.List; // Dodajte ovaj import

@Service
@RequiredArgsConstructor
public class PrijavaService {

    private final PrijavaRepository prijavaRepository;
    private final StudentRepository studentRepository;
    private final IspitRepository ispitRepository;

    public Prijava prijaviIspit(Integer studentId, Integer ispitId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit not found"));

        Prijava novaPrijava = new Prijava();
        novaPrijava.setStudent(student);
        novaPrijava.setIspit(ispit);
        novaPrijava.setVremePrijave(LocalDateTime.now());

        return prijavaRepository.save(novaPrijava);
    }

    // DODATA JE OVA METODA
    public List<Prijava> getPrijaveForStudent(Integer studentId) {
        return prijavaRepository.findByStudentId(studentId);
    }
}