package com.bookapp.backend.domain.model.follower;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Follower extends BaseModel {
    private  User follower;
    private  User followed;

    public Follower(Long id, User follower, User followed) {
        super(id);
        this.follower = Objects.requireNonNull(follower, "Follower darf nicht null sein");
        this.followed = Objects.requireNonNull(followed, "Gefolgter darf nicht null sein");
        if (follower.equals(followed)) {
            throw new IllegalStateException("Ein Benutzer kann sich nicht selbst folgen");
        }
    }

    public Follower() {

    }
}
