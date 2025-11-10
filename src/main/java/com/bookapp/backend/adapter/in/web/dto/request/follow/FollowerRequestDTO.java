package com.bookapp.backend.adapter.in.web.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowerRequestDTO {

    @NotNull(message = "Follower ID darf nicht null sein")
    private Long followerId;

    @NotNull(message = "Followee ID darf nicht null sein")
    private Long followedId;
}
