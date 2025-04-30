package edu.pazyuk.projectfortests.service;

/*
  @author xsour
  @project projectfortests
  @class PatientService
  @version 1.0.0
  @since 24.04.2025 - 14.21
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @PostConstruct
    void init() {
        patientRepository.deleteAll();
        patientRepository.saveAll(List.of(
                new Patient("1", "Alice Smith", "C001", "General checkup"),
                new Patient("2", "Bob Johnson", "C002", "Flu symptoms"),
                new Patient("3", "Charlie Brown", "C003", "Dental care")
        ));
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(String id) {
        if (id == null) return null;
        return patientRepository.findById(id).orElse(null);
    }

    public Patient create(Patient patient) {
        if (patient == null) throw new NullPointerException("Patient cannot be null");
        return patientRepository.save(patient);
    }

    public Patient update(Patient patient) {
        if (patient == null) throw new NullPointerException("Patient cannot be null");
        return patientRepository.save(patient);
    }

    public void deleteById(String id) {
        if (id == null) return;
        patientRepository.deleteById(id);
    }
}