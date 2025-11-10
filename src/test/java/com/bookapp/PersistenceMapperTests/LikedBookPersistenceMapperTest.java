package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.LikedBookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.LikedBookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LikedBookPersistenceMapperTest {

    private UserPersistenceMapper userMapper;
    private BookPersistenceMapper bookMapper;
    private LikedBookPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserPersistenceMapper.class);
        bookMapper = mock(BookPersistenceMapper.class);
        mapper = new LikedBookPersistenceMapper(userMapper, bookMapper);
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        LikedBookEntity entity = new LikedBookEntity();
        UserEntity userEntity = new UserEntity();
        BookEntity bookEntity = new BookEntity();

        userEntity.setId(1L);
        bookEntity.setId(100L);

        entity.setUser(userEntity);
        entity.setBook(bookEntity);

        User user = new User(1L);
        Book book = new Book(100L);

        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(bookMapper.toDomain(bookEntity)).thenReturn(book);

        LikedBook domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(100L, domain.getId()); // basiert auf bookId
        assertEquals(user, domain.getUser());
        assertEquals(book, domain.getBook());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        User user = new User(1L);
        Book book = new Book(100L);
        LikedBook domain = new LikedBook(100L, user, book);

        UserEntity userEntity = new UserEntity();
        BookEntity bookEntity = new BookEntity();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(bookMapper.toEntity(book)).thenReturn(bookEntity);

        LikedBookEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(userEntity, entity.getUser());
        assertEquals(bookEntity, entity.getBook());
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
