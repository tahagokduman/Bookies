package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;

import java.util.List;

public interface IFollowerService extends IBaseService<Follower, FollowerId> {
    List<User> getAllFollowersByUserId(Long id);

    List<User> getAllFollowedUsersByUserId(Long id);

    Long getFollowerCountByUserId(Long userId);

    Long getFollowedCountByUserId(Long userId);

    List<Follower> getFollowerByIds(Long followedId,
                                    Long followerId);

}
