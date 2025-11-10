package com.bookapp.backend.adapter.out.persistence.entity;

import com.bookapp.backend.domain.model.id.UserAvatarId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAvatarEntity {
    @EmbeddedId
    private UserAvatarId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @MapsId("avatarId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id", insertable = false, updatable = false)
    private AvatarEntity avatar;
}
