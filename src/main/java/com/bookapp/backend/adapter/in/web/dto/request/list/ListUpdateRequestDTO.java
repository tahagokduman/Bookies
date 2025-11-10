package com.bookapp.backend.adapter.in.web.dto.request.list;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListUpdateRequestDTO {

    @Size(max = 255)
    private String name;
}
