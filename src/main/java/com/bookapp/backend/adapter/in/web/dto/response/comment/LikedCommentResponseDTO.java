package com.bookapp.backend.adapter.in.web.dto.response.comment;

import com.bookapp.backend.adapter.in.web.dto.response.book.LikedBookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedCommentResponseDTO extends BaseResponseDTO<LikedCommentResponseDTO> {
    private UserShortResponseDTO userId;
    private CommentShortResponseDTO commentId;
}
