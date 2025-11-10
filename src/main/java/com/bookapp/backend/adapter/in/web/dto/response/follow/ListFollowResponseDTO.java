package com.bookapp.backend.adapter.in.web.dto.response.follow;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListFollowResponseDTO extends BaseResponseDTO<ListFollowResponseDTO> {
    private Long userId;
    private Long listId;
    private UserResponseDTO user;
    private ListResponseDTO list;
}
