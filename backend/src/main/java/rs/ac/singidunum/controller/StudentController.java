package rs.ac.singidunum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.singidunum.entity.Student;
import rs.ac.singidunum.service.StudentService;
import java.util.List;

@RestController
@RequestMapping(path = "/api/student")
@CrossOrigin
public class StudentController {

    private final StudentService service;

    // RUČNO DODAT KONSTRUKTOR KOJI REŠAVA PROBLEM
    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        return service.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails) {
        try {
            return ResponseEntity.ok(service.updateStudent(id, studentDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}