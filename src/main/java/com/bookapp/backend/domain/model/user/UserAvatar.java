package com.bookapp.backend.domain.model.user;

import com.bookapp.backend.domain.model.id.UserAvatarId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatar {

    private UserAvatarId id;
    private User user;
    private Avatar avatar;

}
