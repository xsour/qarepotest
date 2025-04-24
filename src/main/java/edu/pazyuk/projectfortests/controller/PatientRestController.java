package edu.pazyuk.projectfortests.controller;

/*
  @author xsour
  @project projectfortests
  @class PatientRestController
  @version 1.0.0
  @since 24.04.2025 - 14.25
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientRestController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        List<Patient> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable String id) {
        Patient patient = patientService.getById(id);
        return (patient != null)
                ? ResponseEntity.ok(patient)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        Patient created = patientService.create(patient);
        // формуємо заголовок Location: /api/patients/{newId}
        URI location = URI.create("/api/patients/" + created.getId());
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(
            @PathVariable String id,
            @RequestBody Patient patient
    ) {
        if (patient.getId() == null) {
            patient.setId(id);
        } else if (!patient.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        if (patientService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        Patient updated = patientService.update(patient);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (patientService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
