package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IListService;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ListServiceIntegrationTest {

    @Autowired
    private IListService listService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User testUser;
    private List testList;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM books_in_list");
        jdbcTemplate.execute("DELETE FROM list_follows");
        jdbcTemplate.execute("DELETE FROM lists");
        jdbcTemplate.execute("DELETE FROM users");

        String uniqueUsername = "testuser-" + UUID.randomUUID().toString().substring(0, 8);
        testUser = new User(
                uniqueUsername,
                uniqueUsername + "@example.com",
                "password",
                LocalDate.now()
        );
        testUser = userRepository.save(testUser);

        testList = new List();
        testList.setUser(testUser);
        testList.setName("Test List");
        testList = listService.create(testList);
    }

    @Test
    public void testCreateAndGetList() {
        assertNotNull(testList.getId());

        Optional<List> found = listService.getById(testList.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test List");
    }

    @Test
    public void testUpdateList() {
        testList.setName("Updated List Name");
        List updated = listService.update(testList.getId(), testList);

        assertThat(updated.getName()).isEqualTo("Updated List Name");

        Optional<List> found = listService.getById(testList.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated List Name");
    }

    @Test
    public void testDeleteList() {
        listService.delete(testList.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            listService.getById(testList.getId()).orElseThrow(
                    () -> new EntityNotFoundException("List should be deleted")
            );
        });
    }

    @Test
    public void testGetAllListByUserId() {
        List list2 = new List();
        list2.setUser(testUser);
        list2.setName("Another List");
        listService.create(list2);

        java.util.List<List> userLists = listService.getAllListByUserId(testUser.getId());

        assertThat(userLists).hasSize(2);
        assertThat(userLists).extracting(List::getName)
                .containsExactlyInAnyOrder("Test List", "Another List");
    }

    @Test
    public void testExploreList() {
        java.util.List<List> exploredLists = listService.exploreList();
        assertThat(exploredLists).isNotNull();
    }

    @Test
    public void testCountListsContainingBook() {
        Long bookId = 1L;
        Long count = listService.countListsContainingBook(bookId);
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testEntityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            listService.getById(999L).orElseThrow(
                    () -> new EntityNotFoundException("List not found")
            );
        });
    }
}