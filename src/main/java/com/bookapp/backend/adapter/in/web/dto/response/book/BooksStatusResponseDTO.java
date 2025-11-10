package com.bookapp.backend.adapter.in.web.dto.response.book;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksStatusResponseDTO extends BaseResponseDTO<BooksStatusResponseDTO> {
    private BookShortResponseDTO book;
    private StatusShortResponseDTO status;
}
