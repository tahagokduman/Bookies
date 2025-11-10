package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BooksStatusEntity;
import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.BooksStatusPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.StatusPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BooksStatusPersistenceMapperTest {

    private BookPersistenceMapper bookMapper;
    private UserPersistenceMapper userMapper;
    private StatusPersistenceMapper statusMapper;
    private BooksStatusPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        bookMapper = mock(BookPersistenceMapper.class);
        userMapper = mock(UserPersistenceMapper.class);
        statusMapper = mock(StatusPersistenceMapper.class);
        mapper = new BooksStatusPersistenceMapper(bookMapper, userMapper, statusMapper);
    }


    @Test
    void toEntity_shouldMapDomainToEntity() {
        User user = new User(2L);
        Book book = new Book(3L);
        Status status = new Status(4L,"Status Name",new User(1L));

        BooksStatus domain = new BooksStatus( user, book, status);

        UserEntity userEntity = new UserEntity();
        BookEntity bookEntity = new BookEntity();
        StatusEntity statusEntity = new StatusEntity();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(bookMapper.toEntity(book)).thenReturn(bookEntity);
        when(statusMapper.toEntity(status)).thenReturn(statusEntity);

        BooksStatusEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(userEntity, entity.getUser());
        assertEquals(bookEntity, entity.getBook());
        assertEquals(statusEntity, entity.getStatus());
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
