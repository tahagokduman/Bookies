package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BooksInListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBooksInListRepository;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBookRepository;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaListRepository;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaUserRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class BooksInListServiceIntegrationTest {

    @Autowired
    private IBooksInListService booksInListService;

    @Autowired
    private IJpaBooksInListRepository booksInListJpaRepo;

    @Autowired
    private IJpaBookRepository bookRepo;

    @Autowired
    private IJpaListRepository listRepo;

    @Autowired
    private IJpaUserRepository userRepo;

    private Long testBookId;
    private Long testListId;
    private BooksInListId testId;

    @BeforeAll
    void setupTestData() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("testuser@example.com");
        user.setFollowersCount(0);
        user.setFollowedPersonCount(0);
        user = userRepo.save(user);

        BookEntity book = new BookEntity();
        book.setTitle("Test Book");
        book.setIsbn("ISBN-0001");
        book.setAuthor("Test Author");
        book.setPublisher("Test Publisher");
        book.setPublishedYear(2025);
        book = bookRepo.save(book);
        testBookId = book.getId();

        ListEntity list = new ListEntity();
        list.setListName("My Reading List");
        list.setUser(user);
        list = listRepo.save(list);
        testListId = list.getId();

        BooksInListEntity bil = new BooksInListEntity();
        testId = new BooksInListId(testListId, testBookId);
        bil.setId(testId);
        bil.setBook(book);
        bil.setList(list);
        booksInListJpaRepo.save(bil);
    }
    @Test
    @Order(1)
    void testGetById() {
        Optional<BooksInList> found = booksInListService.getById(testId);
        assertThat(found).isPresent();

        BooksInList foundBooksInList = found.get();

        Long actualListId = foundBooksInList.getList().getId();
        Long actualBookId = foundBooksInList.getBook().getId();

        assertThat(actualListId).isEqualTo(testId.getListId());
        assertThat(actualBookId).isEqualTo(testId.getBookId());
    }

    @Test
    @Order(2)
    void testFindBooksInList() {
        List<Book> books = booksInListService.findBooksInList(testListId);
        assertThat(books).isNotNull();
        assertThat(books).extracting(Book::getId).contains(testBookId);
    }

    @Test
    @Order(3)
    void testCreateNewBooksInList() {
        BookEntity anotherBook = new BookEntity();
        anotherBook.setTitle("Another Book");
        anotherBook.setIsbn("ISBN-0002");
        anotherBook.setAuthor("Another Author");
        anotherBook.setPublisher("Another Publisher");
        anotherBook.setPublishedYear(2025);
        anotherBook = bookRepo.save(anotherBook);

        Book domainBook = new Book();
        domainBook.setId(anotherBook.getId());

        com.bookapp.backend.domain.model.list.List domainList = new com.bookapp.backend.domain.model.list.List();
        domainList.setId(testListId);

        BooksInList newBooksInList = new BooksInList();
        newBooksInList.setBook(domainBook);
        newBooksInList.setList(domainList);
        newBooksInList.setOrderInList(2);

        BooksInList created = booksInListService.create(newBooksInList);
        assertThat(created).isNotNull();

        BooksInListId createdId = new BooksInListId(testListId, anotherBook.getId());
        Optional<BooksInList> fromDb = booksInListService.getById(createdId);
        assertThat(fromDb).isPresent();
    }

    @Test
    @Order(4)
    void testDeleteBooksInList() {
        booksInListService.delete(testId);
        Optional<BooksInList> deleted = booksInListService.getById(testId);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Order(5)
    void testFullBooksInListFlow() {
        UserEntity user = new UserEntity();
        user.setUsername("integration_user");
        user.setPassword("pass123");
        user.setEmail("integration@test.com");
        user.setFollowersCount(0);
        user.setFollowedPersonCount(0);
        user = userRepo.save(user);

        BookEntity book = new BookEntity();
        book.setTitle("Integration Book");
        book.setIsbn("ISBN-0003");
        book.setAuthor("Test Author");
        book.setPublisher("Test Publisher");
        book.setPublishedYear(2024);
        book = bookRepo.save(book);

        ListEntity list = new ListEntity();
        list.setUser(user);
        list.setListName("Integration List");
        list = listRepo.save(list);

        Book domainBook = new Book();
        domainBook.setId(book.getId());

        com.bookapp.backend.domain.model.list.List domainList = new com.bookapp.backend.domain.model.list.List();
        domainList.setId(list.getId());

        BooksInList booksInList = new BooksInList();
        booksInList.setBook(domainBook);
        booksInList.setList(domainList);
        booksInList.setOrderInList(1);

        BooksInList created = booksInListService.create(booksInList);
        BooksInListId createdId = new BooksInListId(list.getId(), book.getId());
        assertThat(createdId).isNotNull();

        Optional<BooksInList> found = booksInListService.getById(createdId);
        assertThat(found).isPresent();

        List<Book> booksInThisList = booksInListService.findBooksInList(list.getId());
        assertThat(booksInThisList).extracting(Book::getId).contains(book.getId());

        booksInListService.delete(createdId);
        Optional<BooksInList> deleted = booksInListService.getById(createdId);
        assertThat(deleted).isNotPresent();
    }
}
