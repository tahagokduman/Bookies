package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.user.User;

public interface IAuthService {
    User register(User user);

    User login(User user);
}
