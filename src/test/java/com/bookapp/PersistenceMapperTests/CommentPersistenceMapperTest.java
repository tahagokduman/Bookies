package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.CommentPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentPersistenceMapperTest {

    private UserPersistenceMapper userMapper;
    private BookPersistenceMapper bookMapper;
    private CommentPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserPersistenceMapper.class);
        bookMapper = mock(BookPersistenceMapper.class);
        mapper = new CommentPersistenceMapper(userMapper, bookMapper);
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        CommentEntity entity = new CommentEntity();
        entity.setId(1L);

        UserEntity userEntity = new UserEntity();
        BookEntity bookEntity = new BookEntity();

        entity.setUser(userEntity);
        entity.setBook(bookEntity);
        entity.setScore(8);
        entity.setComment("Tolles Buch!");

        User user = new User(2L);
        Book book = new Book(3L);

        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(bookMapper.toDomain(bookEntity)).thenReturn(book);

        Comment domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals(user, domain.getUser());
        assertEquals(book, domain.getBook());
        assertEquals(8, domain.getScore());
        assertEquals("Tolles Buch!", domain.getComment());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        User user = new User(2L);
        Book book = new Book(3L);
        Comment domain = new Comment(1L, user, book, 9, "Sehr spannend!");

        UserEntity userEntity = new UserEntity();
        BookEntity bookEntity = new BookEntity();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(bookMapper.toEntity(book)).thenReturn(bookEntity);

        CommentEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(userEntity, entity.getUser());
        assertEquals(bookEntity, entity.getBook());
        assertEquals(9, entity.getScore());
        assertEquals("Sehr spannend!", entity.getComment());
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_shouldReturnNull_whenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
