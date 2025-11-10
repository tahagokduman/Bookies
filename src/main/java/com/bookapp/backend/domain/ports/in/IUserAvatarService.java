package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.UserAvatar;

public interface IUserAvatarService extends IBaseService<UserAvatar, UserAvatarId>{

    UserAvatar findByUserId(Long userId);

}
