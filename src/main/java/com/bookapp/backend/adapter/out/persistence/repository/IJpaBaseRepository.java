package com.bookapp.backend.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IJpaBaseRepository<T, ID> extends JpaRepository<T, ID> {
}
