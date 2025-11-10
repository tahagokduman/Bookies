package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.ListUseCases;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.out.IListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListUseCasesTest {

    @Mock
    private IListRepository listRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IBooksInListService booksInListService;

    @InjectMocks
    private ListUseCases listUseCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    private List createList(Long id, User user, String name) {
        return new List(id == null ? 0L : id, user, name);
    }

    @Test
    void create_validList_savesAndReturnsList() {
        User user = createUser(1L, "testuser");
        List list = createList(null, user, "Meine Liste");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listRepository.save(list)).thenAnswer(i -> {
            List l = i.getArgument(0);
            l.setId(10L);
            return l;
        });

        List result = listUseCases.create(list);

        assertNotNull(result.getId());
        assertEquals("Meine Liste", result.getName());
        verify(userRepository).findById(1L);
        verify(listRepository).save(list);
    }

    @Test
    void create_nonExistingUser_throwsEntityNotFoundException() {
        User user = createUser(99L, "ghost");
        List list = createList(null, user, "Liste");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            listUseCases.create(list);
        });

        assertTrue(exception.getMessage().contains("User with id 99 was not found"));
        verify(userRepository).findById(99L);
        verify(listRepository, never()).save(any());
    }

    @Test
    void update_existingList_updatesSuccessfully() {
        User user = createUser(1L, "user1");
        List existingList = createList(5L, user, "Alte Liste");
        List updatedList = createList(5L, user, "Neue Liste");

        when(listRepository.findById(5L)).thenReturn(Optional.of(existingList));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listRepository.update(updatedList, 5L)).thenReturn(updatedList);

        List result = listUseCases.update(5L, updatedList);

        assertEquals(5L, result.getId());
        assertEquals("Neue Liste", result.getName());

        verify(listRepository).findById(5L);
        verify(userRepository).findById(1L);
        verify(listRepository).update(updatedList, 5L);
    }

    @Test
    void update_nonExistingList_throwsEntityNotFoundException() {
        User user = createUser(1L, "user1");
        List list = createList(99L, user, "Liste");

        when(listRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            listUseCases.update(99L, list);
        });

        assertTrue(exception.getMessage().contains("List with id 99 was not found"));
        verify(listRepository).findById(99L);
        verify(userRepository, never()).findById(any());
        verify(listRepository, never()).update(any(), anyLong());
    }

    @Test
    void delete_existingList_deletesSuccessfully() {
        List existingList = createList(3L, createUser(1L, "user"), "Liste");

        when(listRepository.findById(3L)).thenReturn(Optional.of(existingList));

        listUseCases.delete(3L);

        verify(listRepository).deleteById(3L);
    }

    @Test
    void delete_nonExistingList_throwsEntityNotFoundException() {
        when(listRepository.findById(42L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            listUseCases.delete(42L);
        });

        assertTrue(exception.getMessage().contains("List with id 42 was not found"));
        verify(listRepository).findById(42L);
        verify(listRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAll_returnsListsWithBooks() {
        User user = createUser(1L, "user1");
        List list = createList(70L, user, "Liste 1");

        java.util.List<Book> books = java.util.List.of(new Book(1L), new Book(2L));

        when(listRepository.findAll()).thenReturn(java.util.List.of(list));
        when(booksInListService.findBooksInList(70L)).thenReturn(books);

        java.util.List<List> result = listUseCases.getAll();

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getBooksList().size());

        verify(listRepository).findAll();
        verify(booksInListService).findBooksInList(70L);
    }
}
