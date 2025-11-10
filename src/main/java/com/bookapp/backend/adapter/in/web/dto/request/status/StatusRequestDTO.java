package com.bookapp.backend.adapter.in.web.dto.request.status;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusRequestDTO {

    private Long id; //ileride güncelleme için gerekli olabilir ama olmayabilir de o yüzden notnull yok

    @NotNull(message = "UserID darf nicht null sein")
    private Long userId;

    @NotNull(message = "Statusname darf nicht null sein")
    private String statusName;

}
