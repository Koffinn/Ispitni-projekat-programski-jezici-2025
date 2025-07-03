package rs.ac.singidunum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.singidunum.entity.Student;
import rs.ac.singidunum.repo.StudentRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Optional<Student> getStudentById(Integer id) {
        return repository.findById(id);
    }

    public Student createStudent(Student student) {
        return repository.save(student);
    }

    public Student updateStudent(Integer id, Student studentDetails) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setIme(studentDetails.getIme());
        student.setPrezime(studentDetails.getPrezime());
        student.setBrojIndeksa(studentDetails.getBrojIndeksa());
        student.setSmer(studentDetails.getSmer());
        student.setLozinka(studentDetails.getLozinka());

        return repository.save(student);
    }

    public void deleteStudent(Integer id) {
        repository.deleteById(id);
    }
}