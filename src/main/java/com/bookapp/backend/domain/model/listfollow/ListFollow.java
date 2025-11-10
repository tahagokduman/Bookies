package com.bookapp.backend.domain.model.listfollow;

import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import lombok.*;

@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class ListFollow {

    private ListFollowId id;
    private List list;
    private User user;
}