package com.bookapp.backend.domain.model.id;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowerId implements Serializable {

    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "followed_person_id")
    private Long followedPersonId;
}