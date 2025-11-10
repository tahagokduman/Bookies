package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.book.Author;
import com.bookapp.backend.domain.ports.in.IAuthorService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorServiceIntegrationTest {

    @Autowired
    private IAuthorService authorService;

    private Long testAuthorId;

    @TestConfiguration
    static class AuthorServiceTestConfig {

        @Bean
        public IAuthorService authorService() {
            return new IAuthorService() {
                private final Map<Long, Author> store = new HashMap<>();
                private long idSequence = 1L;

                @Override
                public Author create(Author author) {
                    author.setId(idSequence++);
                    if (author.getBooks() == null) {
                        author.setBooks(new ArrayList<>());
                    }
                    store.put(author.getId(), author);
                    return author;
                }

                @Override
                public Optional<Author> getById(Long id) {
                    return Optional.ofNullable(store.get(id));
                }

                @Override
                public Author update(Long id, Author author) {
                    if (!store.containsKey(id)) {
                        throw new IllegalArgumentException("Author not found");
                    }
                    store.put(id, author);
                    return author;
                }

                @Override
                public void delete(Long id) {
                    store.remove(id);
                }

                @Override
                public Page<Author> searchAuthor(String name, int page, int size) {
                    List<Author> filtered = store.values().stream()
                            .filter(a -> a.getName() != null && a.getName().toLowerCase().contains(name.toLowerCase()))
                            .collect(Collectors.toList());

                    int start = page * size;
                    int end = Math.min(start + size, filtered.size());
                    List<Author> content = (start >= filtered.size()) ? Collections.emptyList() : filtered.subList(start, end);

                    return new PageImpl<>(content, PageRequest.of(page, size), filtered.size());
                }

                @Override
                public List<Author> getAll() {
                    return new ArrayList<>(store.values());
                }
            };
        }
    }

    @BeforeAll
    void setupTestData() {
        Author author = new Author();
        author.setName("Test Author");
        author.setDescription("A test author description");
        author.setBooks(new ArrayList<>());

        Author created = authorService.create(author);
        testAuthorId = created.getId();

        Assertions.assertNotNull(testAuthorId);
    }

    @Test
    @Order(1)
    void testGetById() {
        Optional<Author> found = authorService.getById(testAuthorId);
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Test Author", found.get().getName());
    }

    @Test
    @Order(2)
    void testSearchAuthor() {
        Page<Author> page = authorService.searchAuthor("Test", 0, 10);
        Assertions.assertNotNull(page);
        Assertions.assertTrue(page.getContent().stream()
                .anyMatch(a -> "Test Author".equals(a.getName())));
    }

    @Test
    @Order(3)
    void testUpdateDescription() {
        Optional<Author> optionalAuthor = authorService.getById(testAuthorId);
        Assertions.assertTrue(optionalAuthor.isPresent());

        Author author = optionalAuthor.get();
        author.updateDescription("Updated description");

        Author updated = authorService.update(testAuthorId, author);

        Assertions.assertEquals("Updated description", updated.getDescription());
    }

    @Test
    @Order(4)
    void testDeleteAuthor() {
        authorService.delete(testAuthorId);
        Optional<Author> deleted = authorService.getById(testAuthorId);
        Assertions.assertFalse(deleted.isPresent());
    }
}
