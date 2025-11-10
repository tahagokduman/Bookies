package com.bookapp.backend.domain.model.status;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class Status extends BaseModel {

    private String status;

    public Status(Long id, String status, User user) {
        super(id != null ? id : 0L);
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Statusname darf nicht leer sein");
        }
        this.status = status;
    }

    public Status(@NotNull(message = "StatusID muss zugewiesen worden sein") Long statusId) {
        super(statusId);
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Statusname darf nicht leer sein");
        }
        this.status = newName;
    }
}
