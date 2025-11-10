package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.follow.FollowerRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.FollowerResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.UserFollowerShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.UserFollowingShortResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.FollowerWebMapper;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class FollowerWebMapperTest {

    private final FollowerWebMapper mapper = new FollowerWebMapper();

    @Test
    void toDomain_shouldMapCorrectly() {
        FollowerRequestDTO dto = new FollowerRequestDTO();
        dto.setFollowerId(1L);
        dto.setFollowedId(2L);

        Follower follower = mapper.toDomain(dto);

        assertThat(follower.getFollower().getId()).isEqualTo(1L);
        assertThat(follower.getFollowed().getId()).isEqualTo(2L);


    }

    @Test
    void toFollowerShortDto_shouldMapCorrectly() {
        User user = new User(1L);
        user.setUsername("ali");

        User followed = new User(2L);

        Follower follower = new Follower(10L, user, followed);
        follower.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));

        UserFollowerShortResponseDTO dto = mapper.toFollowerShortDto(follower);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("ali");
        assertThat(dto.getFollowedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
    }

    @Test
    void toFollowingShortDto_shouldMapCorrectly() {
        User followerUser = new User(1L);

        User followed = new User(2L);
        followed.setUsername("veli");

        Follower follower = new Follower(20L, followerUser, followed);
        follower.setCreatedAt(LocalDateTime.of(2024, 5, 10, 15, 0));

        UserFollowingShortResponseDTO dto = mapper.toFollowingShortDto(follower);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getUsername()).isEqualTo("veli");
        assertThat(dto.getFollowedAt()).isEqualTo(LocalDateTime.of(2024, 5, 10, 15, 0));
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        User followerUser = new User(1L);
        followerUser.setUsername("ali");

        User followedUser = new User(2L);
        followedUser.setUsername("veli");

        Follower follower = new Follower(100L, followerUser, followedUser);
        follower.setCreatedAt(LocalDateTime.of(2025, 1, 1, 12, 0));

        FollowerResponseDTO dto = mapper.toResponseDto(follower);

        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getFollowedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
        assertThat(dto.getFollower().getId()).isEqualTo(1L);
        assertThat(dto.getFollower().getUsername()).isEqualTo("ali");
        assertThat(dto.getFollowing().getId()).isEqualTo(2L);
        assertThat(dto.getFollowing().getUsername()).isEqualTo("veli");
    }
}
