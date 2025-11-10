package com.bookapp.UseCaseTests;
import com.bookapp.backend.application.service.StatusUseCases;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.ports.out.IStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusUseCasesTest {

    private IStatusRepository statusRepository;
    private StatusUseCases statusUseCases;

    @BeforeEach
    void setUp() {
        statusRepository = mock(IStatusRepository.class);
        statusUseCases = new StatusUseCases(statusRepository);
    }

    @Test
    void testGetAll() {
        Status s1 = new Status();
        Status s2 = new Status();
        when(statusRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Status> result = statusUseCases.getAll();

        assertEquals(2, result.size());
        verify(statusRepository).findAll();
    }

    @Test
    void testGetById_existing() {
        Status status = new Status();
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));

        Optional<Status> result = statusUseCases.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(status, result.get());
    }

    @Test
    void testGetById_notFound() {
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            statusUseCases.getById(1L);
        });

        assertEquals("Status with id 1 was not found", ex.getMessage());
    }

    @Test
    void testCreate() {
        Status status = new Status();
        when(statusRepository.save(status)).thenReturn(status);

        Status result = statusUseCases.create(status);

        assertEquals(status, result);
        verify(statusRepository).save(status);
    }

    @Test
    void testDelete_existing() {
        when(statusRepository.findById(1L)).thenReturn(Optional.of(new Status()));

        statusUseCases.delete(1L);

        verify(statusRepository).deleteById(1L);
    }

    @Test
    void testDelete_notFound() {
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> statusUseCases.delete(1L));
    }

    @Test
    void testUpdate_existing() {
        Status status = new Status();
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(statusRepository.update(status, 1L)).thenReturn(status);

        Status result = statusUseCases.update(1L, status);

        assertEquals(status, result);
        verify(statusRepository).update(status, 1L);
    }

    @Test
    void testUpdate_notFound() {
        Status status = new Status();
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> statusUseCases.update(1L, status));
    }
}
