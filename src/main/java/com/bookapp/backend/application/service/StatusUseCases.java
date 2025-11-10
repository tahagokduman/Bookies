package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.ports.in.IStatusService;
import com.bookapp.backend.domain.ports.out.IStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusUseCases implements IStatusService {

    private final IStatusRepository statusRepository;

    @Override
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> getById(Long id) {
        return Optional.of(
                statusRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Status with id " + id + " was not found"))
        );
    }

    @Override
    public Status create(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        ensureStatusExists(id);
        statusRepository.deleteById(id);
    }

    @Override
    public Status update(Long id, Status status) {
        ensureStatusExists(id);
        return statusRepository.update(status, id);
    }

    private void ensureStatusExists(Long id) {
        if (statusRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Status with id " + id + " was not found");
        }
    }

    @Override
    public boolean existsByName(String name) {
        return statusRepository.existsByName(name);
    }

    @Override
    public Status findByName(String name) {
        return statusRepository.findByName(name);
    }
}
