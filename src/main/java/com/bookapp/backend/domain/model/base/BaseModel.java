package com.bookapp.backend.domain.model.base;

import com.bookapp.backend.domain.model.id.AuthorBookId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseModel {
    protected Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public BaseModel(long id) {
        this.id = id;
    }
}