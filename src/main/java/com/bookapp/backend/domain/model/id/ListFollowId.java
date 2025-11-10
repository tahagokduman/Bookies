package com.bookapp.backend.domain.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class ListFollowId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "list_id")
    private Long listId;
}
