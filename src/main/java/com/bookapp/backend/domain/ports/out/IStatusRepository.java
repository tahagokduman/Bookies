package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.status.Status;

public interface IStatusRepository extends IBaseRepository<Status, Long>{
    boolean existsByName(String name);
    Status findByName(String name);
}
