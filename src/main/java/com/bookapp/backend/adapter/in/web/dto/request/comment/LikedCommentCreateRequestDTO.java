package com.bookapp.backend.adapter.in.web.dto.request.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedCommentCreateRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long commentId;
}
