package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface IUserRepository extends IBaseRepository<User, Long>{
    void deleteAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> searchUser(String username, Pageable pageable);
}
