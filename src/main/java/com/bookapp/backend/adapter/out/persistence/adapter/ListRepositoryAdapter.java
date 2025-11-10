package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.ListPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBooksInListRepository;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaListRepository;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.ports.out.IListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ListRepositoryAdapter implements IListRepository {

    @Autowired
    private IJpaListRepository iJpaListRepository;

    @Autowired
    IJpaBooksInListRepository iJpaBooksInListRepository;

    @Autowired
    private ListPersistenceMapper listMapper;

    @Override
    public java.util.List<List> findAll() {
        return iJpaListRepository
                .findAll()
                .stream()
                .map(listMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<List> findById(Long aLong) {
        return iJpaListRepository.findById(aLong).map(listMapper::toDomain);
    }

    @Override
    public List save(List entity) {
        ListEntity listEntity = listMapper.toEntity(entity);
        ListEntity savedEntity = iJpaListRepository.save(listEntity);
        return listMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long aLong) {
        iJpaListRepository.deleteById(aLong);
    }

    @Override
    public List update(List dto, Long aLong) {
        ListEntity list = listMapper.toEntity(dto);
        list.setId(aLong);
        ListEntity updatedList = iJpaListRepository.save(list);
        return listMapper.toDomain(updatedList);
    }

    @Override
    public java.util.List<List> getAllListByUserId(Long id) {
        return iJpaListRepository.getAllListByUserId(id).stream().map(listMapper::toDomain).toList();
    }

    @Override
    public java.util.List<List> exploreList() {
        return iJpaListRepository.getAllListByMoreFollow().stream().map(listMapper::toDomain).toList();
    }
    @Override
    public Long countListsContainingBook(Long bookId) {
        return iJpaListRepository.countListsContainingBook(bookId);
    }

}