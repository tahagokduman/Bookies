package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IJpaStatusRepository extends IJpaBaseRepository<StatusEntity, Long>{
    boolean existsByName(String name);
    StatusEntity findByName(String name);
}
