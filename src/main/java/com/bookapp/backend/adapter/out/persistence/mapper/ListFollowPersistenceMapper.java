package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.ListFollowEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListFollowPersistenceMapper {

    private final ListPersistenceMapper listMapper;
    private final UserPersistenceMapper userMapper;

    public ListFollow toDomain(ListFollowEntity entity) {
        if (entity == null) return null;

        ListFollow domain = new ListFollow();
        domain.setId(entity.getId());
        domain.setList(listMapper.toDomain(entity.getList()));
        domain.setUser(userMapper.toDomain(entity.getUser()));
        return domain;
    }

    public ListFollowEntity toEntity(ListFollow domain) {
        if (domain == null) return null;

        ListFollowEntity entity = new ListFollowEntity();
        entity.setId(domain.getId());

        var user = new UserEntity();
        user.setId(domain.getId().getUserId());
        entity.setUser(user);

        var list = new ListEntity();
        list.setId(domain.getId().getListId());
        entity.setList(list);

        return entity;
    }
}
