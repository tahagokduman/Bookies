package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.BooksInListEntity;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BooksInListPersistenceMapper {

    private final BookPersistenceMapper bookMapper;
    private final ListPersistenceMapper listMapper;

    public BooksInList toDomain(BooksInListEntity entity) {
        if (entity == null) return null;
        BooksInList list = new BooksInList();
        list.setBook(bookMapper.toDomain(entity.getBook()));
        list.setList(listMapper.toDomain(entity.getList()));
        list.setId(entity.getBook().getId()); // IT CAN BE WRONG
        return list;
    }

    public BooksInListEntity toEntity(BooksInList domain) {
        if (domain == null) return null;

        BooksInListEntity entity = new BooksInListEntity();
        entity.setId(new BooksInListId());
        entity.setList(listMapper.toEntity(domain.getList()));
        entity.setBook(bookMapper.toEntity(domain.getBook()));
        return entity;
    }

}
