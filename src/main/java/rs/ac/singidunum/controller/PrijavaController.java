package rs.ac.singidunum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.ac.singidunum.dto.PrijavaDto;
import rs.ac.singidunum.entity.Prijava;
import rs.ac.singidunum.service.PrijavaService;

import java.util.List; // Dodajte ovaj import

@RestController
@RequestMapping("/api/prijava")
@CrossOrigin
@RequiredArgsConstructor
public class PrijavaController {

    private final PrijavaService service;

    @PostMapping
    public Prijava prijaviIspit(@RequestBody PrijavaDto prijavaDto) {
        return service.prijaviIspit(prijavaDto.getStudentId(), prijavaDto.getIspitId());
    }

    // DODAT JE OVAJ ENDPOINT
    @GetMapping("/student/{studentId}")
    public List<Prijava> getPrijaveForStudent(@PathVariable Integer studentId) {
        return service.getPrijaveForStudent(studentId);
    }
}