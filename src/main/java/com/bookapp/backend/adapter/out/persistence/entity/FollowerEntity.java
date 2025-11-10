package com.bookapp.backend.adapter.out.persistence.entity;

import com.bookapp.backend.domain.model.id.FollowerId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "followers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FollowerEntity extends BaseEntity<FollowerId> {

    @EmbeddedId
    private FollowerId id;

    @MapsId("followerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private UserEntity follower;

    @MapsId("followedPersonId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_person_id", insertable = false, updatable = false)
    private UserEntity followedPerson;
}