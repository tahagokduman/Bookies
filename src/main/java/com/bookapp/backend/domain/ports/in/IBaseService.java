package com.bookapp.backend.domain.ports.in;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T, ID> {
    List<T> getAll();

    Optional<T> getById(ID id);

    T create(T object);

    void delete(ID id);

    T update(ID id, T object);
}
