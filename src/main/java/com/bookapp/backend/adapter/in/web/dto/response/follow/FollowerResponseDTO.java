package com.bookapp.backend.adapter.in.web.dto.response.follow;

import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowerResponseDTO extends RepresentationModel<FollowerResponseDTO> {
    private Long id;
    private UserShortResponseDTO follower;
    private UserShortResponseDTO following;
    private LocalDateTime followedAt; //çok da gerekli olmayabilir
}
