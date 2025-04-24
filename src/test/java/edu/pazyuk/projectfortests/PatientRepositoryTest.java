package edu.pazyuk.projectfortests;

/*
  @author xsour
  @project projectfortests
  @class PatientServiceTest
  @version 1.0.0
  @since 24.04.2025 - 15.30
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.repository.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository underTest;

    private Patient alice;
    private Patient bob;
    private Patient charlie;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();

        alice   = new Patient("1", "Alice Smith",   "C001", "###test – initial");
        bob     = new Patient("2", "Bob Johnson",   "C002", "###test – initial");
        charlie = new Patient("3", "Charlie Brown", "C003", "###test – initial");

        underTest.saveAll(Arrays.asList(alice, bob, charlie));
    }

    @AfterEach
    void tearDown() {
        var toDelete = underTest.findAll().stream()
                .filter(p -> p.getDescription().contains("###test"))
                .collect(Collectors.toList());
        underTest.deleteAll(toDelete);
    }

    @Test
    void shouldGenerateIdForNewPatient() {
        // given
        Patient newPatient = new Patient("David Lee", "C004", "###test – new");
        String beforeId = newPatient.getId();
        assertNull(beforeId, "ID до збереження повинен бути null");

        // when
        Patient saved = underTest.save(newPatient);

        // then
        assertNotNull(saved.getId(), "ID після save має бути згенерований");
        assertNotEquals(beforeId, saved.getId(),
                "ID після save має відрізнятися від початкового (null)");
    }

    @Test
    void shouldFindById() {
        Optional<Patient> opt = underTest.findById(alice.getId());
        assertTrue(opt.isPresent());
        assertEquals("Alice Smith", opt.get().getName());
    }

    @Test
    void shouldFindAllPatients() {
        List<Patient> all = underTest.findAll();
        assertEquals(3, all.size());
        var names = all.stream().map(Patient::getName).toList();
        assertTrue(names.containsAll(List.of("Alice Smith","Bob Johnson","Charlie Brown")));
    }

    @Test
    void shouldReturnEmptyOptionalForUnknownId() {
        assertTrue(underTest.findById("no-such-id").isEmpty());
    }

    @Test
    void shouldUpdatePatient() {
        Patient toUpdate = underTest.findById(bob.getId()).orElseThrow();
        toUpdate.setDescription("UPDATED DESCRIPTION");
        underTest.save(toUpdate);
        Patient fetched = underTest.findById(bob.getId()).orElseThrow();
        assertEquals("UPDATED DESCRIPTION", fetched.getDescription());
    }

    @Test
    void shouldDeleteById() {
        underTest.deleteById(charlie.getId());
        assertFalse(underTest.existsById(charlie.getId()));
    }

    @Test
    void shouldDeleteAllPatients() {
        underTest.deleteAll();
        assertEquals(0, underTest.count());
    }

    @Test
    void shouldCountPatientsCorrectly() {
        underTest.deleteAll();
        underTest.saveAll(Arrays.asList(alice, bob));
        assertEquals(2, underTest.count());
    }

    @Test
    void shouldCheckExistenceById() {
        assertTrue(underTest.existsById(alice.getId()));
        assertFalse(underTest.existsById("no-such"));
    }

    @Test
    void shouldSaveAllPatientsInBatch() {
        underTest.deleteAll();
        Patient p1 = new Patient("Eve Adams", "C005", "###test – batch");
        Patient p2 = new Patient("Frank Wright","C006", "###test – batch");
        underTest.saveAll(List.of(p1, p2));
        List<String> names = underTest.findAll().stream()
                .map(Patient::getName).toList();
        assertTrue(names.containsAll(List.of("Eve Adams","Frank Wright")));
    }
}