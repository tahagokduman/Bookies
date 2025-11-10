package com.bookapp.backend.domain.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class Password {
    private final String value;

    public Password(String value) {
        this.value = Objects.requireNonNull(value, "Passwort darf nicht null sein");
    }
}
