package rs.ac.singidunum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.singidunum.entity.Ispit;
import rs.ac.singidunum.service.IspitService;

import java.util.List;

@RestController
@RequestMapping("/api/ispit")
@CrossOrigin
@RequiredArgsConstructor
public class IspitController {

    private final IspitService service;

    @GetMapping
    public List<Ispit> getIspiti() {
        return service.getAllIspiti();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ispit> getIspitById(@PathVariable Integer id) {
        return service.getIspitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ispit createIspit(@RequestBody Ispit ispit) {
        return service.createIspit(ispit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ispit> updateIspit(@PathVariable Integer id, @RequestBody Ispit ispitDetails) {
        try {
            return ResponseEntity.ok(service.updateIspit(id, ispitDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIspit(@PathVariable Integer id) {
        service.deleteIspit(id);
        return ResponseEntity.noContent().build();
    }
}