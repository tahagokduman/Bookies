package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBooksInListRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.out.IBooksInListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListPersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public List toDomain(ListEntity entity) {
        if (entity == null) return null;
        List list = new List();
        list.setId(entity.getId());
        list.setUpdatedAt(entity.getUpdatedAt());
        list.setCreatedAt(entity.getCreatedAt());
        list.setName(entity.getListName());
        list.setUser(userMapper.toDomain(entity.getUser()));
        return list;
    }

    public ListEntity toEntity(List domain) {
        if (domain == null) return null;
        ListEntity entity = new ListEntity();
        entity.setId(domain.getId());
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setListName(domain.getName());
        return entity;
    }
}
