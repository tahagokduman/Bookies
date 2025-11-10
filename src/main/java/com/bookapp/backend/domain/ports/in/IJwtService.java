package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.user.User;
import io.jsonwebtoken.Claims;

public interface IJwtService {
    String generateToken(User user);

    Claims parseToken(String token);
}
