package com.bookapp.backend.adapter.in.web.dto.response.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListShortResponseDTO {
    private Long id;
    private String title;
}
