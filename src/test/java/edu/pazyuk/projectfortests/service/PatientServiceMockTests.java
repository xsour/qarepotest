package edu.pazyuk.projectfortests.service;

/*
  @author xsour
  @project projectfortests
  @class PatientServiceMockTests
  @version 1.0.0
  @since 17.05.2025 - 20.56
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class PatientServiceMockTests {

    @Mock
    private PatientRepository mockRepository;

    @InjectMocks
    private PatientService underTest;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Test
    @DisplayName("1. getAll() — коли є записи, повертає список")
    void testGetAllReturnsList() {
        // given
        List<Patient> patients = List.of(
                new Patient("1", "Alice", "C001", "Note1"),
                new Patient("2", "Bob",   "C002", "Note2")
        );
        given(mockRepository.findAll()).willReturn(patients);

        // when
        List<Patient> result = underTest.getAll();

        // then
        assertThat(result).hasSize(2)
                .extracting(Patient::getName)
                .containsExactly("Alice", "Bob");
        then(mockRepository).should().findAll();
    }

    @Test
    @DisplayName("2. getAll() — коли записів нема, повертає пустий список")
    void testGetAllReturnsEmptyList() {
        given(mockRepository.findAll()).willReturn(List.of());
        List<Patient> result = underTest.getAll();
        assertTrue(result.isEmpty());
        then(mockRepository).should().findAll();
    }

    @Test
    @DisplayName("3. getById() — існуючий id повертає пацієнта")
    void testGetByIdExists() {
        Patient alice = new Patient("1", "Alice", "C001", "Note");
        given(mockRepository.findById("1"))
                .willReturn(Optional.of(alice));

        Patient result = underTest.getById("1");

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        then(mockRepository).should().findById("1");
    }

    @Test
    @DisplayName("4. getById() — неіснуючий id повертає null")
    void testGetByIdNotExists() {
        given(mockRepository.findById("99"))
                .willReturn(Optional.empty());

        Patient result = underTest.getById("99");

        assertNull(result);
        then(mockRepository).should().findById("99");
    }

    @Test
    @DisplayName("5. getById(null) — не кидає винятку і не викликає репозиторій")
    void testGetByIdNull() {
        assertDoesNotThrow(() -> {
            Patient result = underTest.getById(null);
            assertNull(result);
        });
        then(mockRepository).should(never()).findById(any());
    }


    @Test
    @DisplayName("6. create() — happy path, зберігає пацієнта")
    void testCreatePatientHappyPath() {
        Patient toCreate = new Patient("3", "Charlie", "C003", "Check");
        given(mockRepository.save(any(Patient.class)))
                .willAnswer(inv -> inv.getArgument(0));

        Patient created = underTest.create(toCreate);

        then(mockRepository).should().save(patientCaptor.capture());
        Patient saved = patientCaptor.getValue();

        assertAll("saved patient",
                () -> assertEquals("3", saved.getId()),
                () -> assertEquals("Charlie", saved.getName()),
                () -> assertEquals("C003", saved.getCode()),
                () -> assertEquals("Check", saved.getDescription())
        );
        assertSame(saved, created);
    }

    @Test
    @DisplayName("7. create(null) — кидає NullPointerException")
    void testCreatePatientNullThrows() {
        assertThrows(NullPointerException.class, () -> underTest.create(null));
        then(mockRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("8. update() — happy path, оновлює пацієнта")
    void testUpdatePatientHappyPath() {
        Patient updatedInfo = new Patient("2", "Bobby", "C002", "New note");
        given(mockRepository.save(any(Patient.class)))
                .willAnswer(inv -> inv.getArgument(0));

        Patient result = underTest.update(updatedInfo);

        then(mockRepository).should().save(patientCaptor.capture());
        Patient saved = patientCaptor.getValue();

        assertAll("updated patient",
                () -> assertEquals("2", saved.getId()),
                () -> assertEquals("Bobby", saved.getName()),
                () -> assertEquals("New note", saved.getDescription())
        );
        assertSame(saved, result);
    }

    @Test
    @DisplayName("9. update(null) — кидає NullPointerException")
    void testUpdatePatientNullThrows() {
        assertThrows(NullPointerException.class, () -> underTest.update(null));
        then(mockRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("10. deleteById() — викликає репозиторій із правильним id")
    void testDeleteByIdInvokesRepository() {
        doNothing().when(mockRepository).deleteById("5");

        underTest.deleteById("5");

        then(mockRepository).should(times(1)).deleteById("5");
    }
}