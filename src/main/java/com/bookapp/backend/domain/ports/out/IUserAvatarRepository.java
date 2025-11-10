package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.UserAvatar;


public interface IUserAvatarRepository extends IBaseRepository<UserAvatar, UserAvatarId> {

    UserAvatar findByUserId(Long userId);
}
