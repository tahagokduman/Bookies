package com.bookapp.backend.domain.model.base;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@ToString
@EqualsAndHashCode
public final class NonNegativeInteger {

    private final int value;

    public NonNegativeInteger(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Wert darf nicht negativ sein: " + value);
        }
        this.value = value;
    }

    public NonNegativeInteger increment() {
        return new NonNegativeInteger(value + 1);
    }

    public NonNegativeInteger decrement() {
        if (value == 0) {
            throw new IllegalStateException("Wert kann nicht weiter verringert werden");
        }
        return new NonNegativeInteger(value - 1);
    }
}
