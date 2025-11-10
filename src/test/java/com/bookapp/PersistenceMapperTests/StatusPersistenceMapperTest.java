package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.StatusPersistenceMapper;
import com.bookapp.backend.domain.model.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPersistenceMapperTest {

    private StatusPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new StatusPersistenceMapper(null);
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        StatusEntity entity = new StatusEntity();
        entity.setId(5L);
        entity.setName("Abgebrochen");

        Status domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(5L, domain.getId());
        assertEquals("Abgebrochen", domain.getStatus());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        Status domain = new Status();
        domain.setId(10L);
        domain.setStatus("Wird gelesen");

        StatusEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(10L, entity.getId());
        assertEquals("Wird gelesen", entity.getName());
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
