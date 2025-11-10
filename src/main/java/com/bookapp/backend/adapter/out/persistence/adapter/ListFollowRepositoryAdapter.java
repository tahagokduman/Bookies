package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.ListFollowEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.ListFollowPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.ListPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaListFollowRepository;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.ports.out.IListFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ListFollowRepositoryAdapter implements IListFollowRepository {

    private final IJpaListFollowRepository jpaRepository;
    private final ListFollowPersistenceMapper mapper;
    private final ListPersistenceMapper listPersistenceMapper;

    @Override
    public List<ListFollow> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ListFollow> findById(ListFollowId id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ListFollow save(ListFollow domain) {
        ListFollowEntity entity = mapper.toEntity(domain);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(ListFollowId id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public ListFollow update(ListFollow domain, ListFollowId id) {
        if (!jpaRepository.existsById(id)) {
            throw new IllegalArgumentException("ListFollow not found: " + id);
        }
        ListFollowEntity entity = mapper.toEntity(domain);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Long countById_ListId(Long listId) {
        return jpaRepository.countById_ListId(listId);
    }

    @Override
    public boolean existsByUserIdAndListId(Long userId, Long listId) {
        return jpaRepository.existsById(new ListFollowId(userId, listId));
    }

    @Override
    public Long countFollowersOfListsContainingBook(Long bookId) {
        return jpaRepository.countFollowersOfListsContainingBook(bookId);
    }

    @Override
    public List<com.bookapp.backend.domain.model.list.List> getFollowedListsByUserId(Long userId) {
        return jpaRepository.getFollowedListByUserId(userId)
                .stream()
                .map(listPersistenceMapper::toDomain)
                .toList();
    }

}
