package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IStatusService;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StatusServiceIntegrationTest {

    @Autowired
    private IStatusService statusService;

    @Autowired
    private IUserRepository userRepository;

    private User testUser;
    private Status testStatus;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com", "password", null);
        testUser = userRepository.save(testUser);

        testStatus = new Status(1L, "Test Status", testUser);
        testStatus = statusService.create(testStatus);
    }

    @Test
    public void testCreateAndGetStatus() {
        assertNotNull(testStatus.getId());

        Status found = statusService.getById(testStatus.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo("Test Status");
    }

    @Test
    public void testUpdateStatus() {
        testStatus.rename("Updated Status");
        Status updated = statusService.update(testStatus.getId(), testStatus);

        assertThat(updated.getStatus()).isEqualTo("Updated Status");

        Status found = statusService.getById(testStatus.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo("Updated Status");
    }

    @Test
    public void testDeleteStatus() {
        statusService.delete(testStatus.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            statusService.getById(testStatus.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Status should be deleted")
            );
        });
    }

    @Test
    public void testExistsByName() {
        boolean exists = statusService.existsByName("Test Status");
        assertTrue(exists);

        boolean notExists = statusService.existsByName("Non-existent Status");
        assertFalse(notExists);
    }

    @Test
    public void testFindByName() {
        Status found = statusService.findByName("Test Status");
        assertNotNull(found);
        assertThat(found.getStatus()).isEqualTo("Test Status");

        assertThrows(EntityNotFoundException.class, () -> {
            Status notFound = statusService.findByName("Non-existent Status");
            if (notFound == null) {
                throw new EntityNotFoundException("Status not found");
            }
        });
    }

    @Test
    public void testStatusValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Status(1L, "", testUser);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            testStatus.rename("");
        });
    }
}