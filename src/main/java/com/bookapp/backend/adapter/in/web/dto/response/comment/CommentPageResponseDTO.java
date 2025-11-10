package com.bookapp.backend.adapter.in.web.dto.response.comment;

import com.bookapp.backend.adapter.in.web.dto.response.common.PageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageResponseDTO extends PageResponseDTO<CommentShortResponseDTO> {
    private List<CommentShortResponseDTO> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
