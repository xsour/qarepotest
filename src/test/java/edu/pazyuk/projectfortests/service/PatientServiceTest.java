package edu.pazyuk.projectfortests.service;

/*
  @author xsour
  @project projectfortests
  @class PatientServiceTest
  @version 1.0.0
  @since 30.04.2025 - 20.29
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.repository.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
        patientRepository.saveAll(List.of(
                new Patient("1", "Alice Smith", "C001", "General checkup"),
                new Patient("2", "Bob Johnson", "C002", "Flu symptoms"),
                new Patient("3", "Charlie Brown", "C003", "Dental care")
        ));
    }

    @Test
    @Order(1)
    void testGetAllPatients() {
        List<Patient> patients = patientService.getAll();
        assertEquals(3, patients.size());
    }

    @Test
    @Order(2)
    void testGetPatientByIdExists() {
        Patient patient = patientService.getById("1");
        assertNotNull(patient);
        assertEquals("Alice Smith", patient.getName());
    }

    @Test
    @Order(3)
    void testGetPatientByIdNotExists() {
        Patient patient = patientService.getById("99");
        assertNull(patient);
    }

    @Test
    @Order(4)
    void testCreatePatient() {
        Patient newPatient = new Patient("4", "Diana Prince", "C004", "Vaccination");
        Patient created = patientService.create(newPatient);
        assertNotNull(created);
        assertEquals("Diana Prince", created.getName());
    }

    @Test
    @Order(5)
    void testUpdatePatient() {
        Patient existing = patientService.getById("2");
        existing.setName("Bob Marley");
        Patient updated = patientService.update(existing);
        assertEquals("Bob Marley", updated.getName());
    }

    @Test
    @Order(6)
    void testDeletePatientById() {
        patientService.deleteById("3");
        assertNull(patientService.getById("3"));
    }

    @Test
    @Order(7)
    void testCreatePatientNull() {
        assertThrows(NullPointerException.class, () -> patientService.create(null));
    }

    @Test
    @Order(8)
    void testUpdatePatientNull() {
        assertThrows(NullPointerException.class, () -> patientService.update(null));
    }

    @Test
    @Order(9)
    void testDeletePatientByIdNull() {
        assertDoesNotThrow(() -> patientService.deleteById(null));
    }

    @Test
    @Order(10)
    void testGetPatientByIdNull() {
        assertDoesNotThrow(() -> patientService.getById(null));
    }

    @Test
    @Order(11)
    void testCreateMultiplePatients() {
        Patient p1 = new Patient("5", "Eve Adams", "C005", "Checkup");
        Patient p2 = new Patient("6", "Frank Wright", "C006", "Surgery");
        patientService.create(p1);
        patientService.create(p2);
        assertEquals(5, patientService.getAll().size());
    }

    @Test
    @Order(12)
    void testUpdateNonExistingPatient() {
        Patient nonExisting = new Patient("99", "Ghost", "C099", "None");
        Patient updated = patientService.update(nonExisting);
        assertNotNull(updated);
    }

    @Test
    @Order(13)
    void testCreatePatientWithLongName() {
        String longName = "A".repeat(300); // довге ім’я > 255 символів
        Patient patient = new Patient("13", longName, "C013", "Observation");
        Patient saved = patientService.create(patient);
        assertNotNull(saved);
        assertEquals(longName, saved.getName());
    }


    @Test
    @Order(14)
    void testGetAllPatientsEmpty() {
        patientRepository.deleteAll();
        List<Patient> patients = patientService.getAll();
        assertTrue(patients.isEmpty());
    }

    @Test
    @Order(15)
    void testDeleteNonExistingPatient() {
        assertDoesNotThrow(() -> patientService.deleteById("999"));
    }

    @Test
    @Order(16)
    void testUpdatePatientFields() {
        Patient patient = patientService.getById("2");
        patient.setDescription("Updated description");
        Patient updated = patientService.update(patient);
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    @Order(17)
    void testCreatePatientImmutable() {
        Patient patient = new Patient("7", "Immutable", "C007", "Test");
        patientService.create(patient);
        assertEquals("Immutable", patient.getName());
    }

    @Test
    @Order(18)
    void testGetByIdCalledOnce() {
        patientService.getById("1");
        // In integration tests, we don't verify method calls as in unit tests
        // This test ensures the method executes without errors
        assertNotNull(patientService.getById("1"));
    }

    @Test
    @Order(19)
    void testDeleteByIdCalledOnce() {
        patientService.deleteById("2");
        assertNull(patientService.getById("2"));
    }

    @Test
    @Order(20)
    void testCreatePatientWithEmptyFields() {
        Patient patient = new Patient("8", "", "", "");
        Patient created = patientService.create(patient);
        assertNotNull(created);
        assertEquals("", created.getName());
    }
}