package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.follow.FollowerRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.FollowerResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.UserFollowerShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.UserFollowingShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class FollowerWebMapper {

    public Follower toDomain(FollowerRequestDTO dto) {
        System.out.println("FollowerId: " + dto.getFollowerId());
        System.out.println("FollowedId: " + dto.getFollowedId());
        User follower = new User(dto.getFollowerId());
        User followed = new User(dto.getFollowedId());
        if (dto.getFollowerId() == null || dto.getFollowedId() == null) {
            throw new IllegalArgumentException("FollowerId oder FollowedId darf nicht null sein");
        }
        return new Follower(follower.getId(), follower, followed);
    }


    public UserFollowerShortResponseDTO toFollowerShortDto(Follower follower) {
        UserFollowerShortResponseDTO dto = new UserFollowerShortResponseDTO();
        dto.setId(follower.getFollower().getId());
        dto.setUsername(follower.getFollower().getUsername());
        dto.setFollowedAt(follower.getCreatedAt());
        return dto;
    }

    public UserFollowingShortResponseDTO toFollowingShortDto(Follower follower) {
        UserFollowingShortResponseDTO dto = new UserFollowingShortResponseDTO();
        dto.setId(follower.getFollowed().getId());
        dto.setUsername(follower.getFollowed().getUsername());
        dto.setFollowedAt(follower.getCreatedAt());
        return dto;
    }

    public FollowerResponseDTO toResponseDto(Follower entity) {
        FollowerResponseDTO dto = new FollowerResponseDTO();
        dto.setId(entity.getId());
        dto.setFollowedAt(entity.getCreatedAt());

        UserShortResponseDTO followerDto = new UserShortResponseDTO();
        followerDto.setId(entity.getFollower().getId());
        followerDto.setUsername(entity.getFollower().getUsername());
        dto.setFollower(followerDto);

        UserShortResponseDTO followingDto = new UserShortResponseDTO();
        followingDto.setId(entity.getFollowed().getId());
        followingDto.setUsername(entity.getFollowed().getUsername());
        dto.setFollowing(followingDto);

        return dto;
    }

}

