package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.user.User;
import org.springframework.data.domain.Page;

public interface IUserService extends IBaseService<User, Long> {
    Page<User> getUsersPaging(int page, int size);

    Page<User> searchUser(String username, int page, int size);
}
