package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.status.Status;

public interface IStatusService extends IBaseService<Status, Long> {
    boolean existsByName(String name);
    Status findByName(String name);
}
