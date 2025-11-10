package com.bookapp.backend.application.config;

import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.StatusPersistenceMapper;
import com.bookapp.backend.domain.model.book.StatusConstants;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.ports.out.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusInitializer implements CommandLineRunner {

    private final IStatusRepository statusRepository;

    @Override
    public void run(String... args) {
        createIfNotExists(StatusConstants.WILL_READ);
        createIfNotExists(StatusConstants.ALREADY_READ);
    }

    private void createIfNotExists(String name) {
        if (!statusRepository.existsByName(name)) {
            Status status = new Status();
            status.setStatus(name);
            statusRepository.save(status);
        }
    }
}
