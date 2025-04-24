package edu.pazyuk.projectfortests;

/*
  @author xsour
  @project projectfortests
  @class PatientRepositoryAuditingTest
  @version 1.0.0
  @since 24.04.2025 - 16.38
*/

import edu.pazyuk.projectfortests.model.Patient;
import edu.pazyuk.projectfortests.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PatientRepositoryAuditingTest {

    @Autowired
    private PatientRepository underTest;

    // Прямо читаємо системне ім'я, яке поверне AuditorAwareImpl
    private final String systemUser = System.getProperty("user.name");

    @BeforeEach
    void clean() {
        underTest.deleteAll();
    }

    @Test
    void shouldPopulateCreatedByAndCreatedDate() {
        // given
        Patient p = new Patient(null, "Anna Audit", "A100", "audit-check");

        // when
        Patient saved = underTest.save(p);

        // then
        assertNotNull(saved.getCreatedBy(),   "createdBy має бути заповнений");
        assertEquals(systemUser, saved.getCreatedBy(),
                "createdBy має братися з AuditorAwareImpl");
        assertNotNull(saved.getCreatedDate(), "createdDate має бути проставлений");
        assertTrue(saved.getCreatedDate().isBefore(Instant.now().plusSeconds(1)),
                "createdDate має бути до зараз");

        // при першому save lastModified має збігатися з created
        assertEquals(systemUser, saved.getLastModifiedBy());
        assertEquals(saved.getCreatedDate(), saved.getLastModifiedDate());
    }

    @Test
    void shouldPopulateLastModifiedOnUpdate() throws InterruptedException {
        // given — спочатку збережемо новий
        Patient p = underTest.save(
                new Patient(null, "Bob Audit", "A101", "audit-update")
        );
        Instant createdDate = p.getCreatedDate();

        Thread.sleep(20); // щоб різниця була помітною

        // when — оновлюємо опис
        p.setDescription("audit-updated");
        Patient updated = underTest.save(p);

        // then
        assertEquals(systemUser, updated.getLastModifiedBy(),
                "lastModifiedBy має братися з AuditorAwareImpl");
        assertTrue(updated.getLastModifiedDate().isAfter(createdDate),
                "lastModifiedDate має бути пізніше за createdDate");
    }
}