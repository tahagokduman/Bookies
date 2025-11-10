package com.bookapp.backend.domain.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class Email {
    private final String value;

    public Email(String value) {
        this.value = Objects.requireNonNull(value, "E-Mail darf nicht null sein");
    }
}
