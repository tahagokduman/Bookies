package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.LikedBookEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.LikedBookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaLikedBookRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.ports.out.ILikedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LikedBookRepositoryAdapter implements ILikedBookRepository {

    @Autowired
    private IJpaLikedBookRepository iJpaLikedBookRepository;

    @Autowired
    private LikedBookPersistenceMapper likedBookMapper;

    @Autowired
    private BookPersistenceMapper bookPersistenceMapper;

    @Override
    public List<LikedBook> findAll() {
        return iJpaLikedBookRepository
                .findAll()
                .stream()
                .map(likedBookMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<LikedBook> findById(LikedBookId id) {
        return iJpaLikedBookRepository.findById(id).map(likedBookMapper::toDomain);
    }

    @Override
    public LikedBook save(LikedBook entity) {
        LikedBookEntity likedBookEntity = likedBookMapper.toEntity(entity);
        LikedBookEntity savedEntity = iJpaLikedBookRepository.save(likedBookEntity);
        return likedBookMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(LikedBookId id) {
        iJpaLikedBookRepository.deleteById(id);
    }

    @Override
    public LikedBook update(LikedBook dto, LikedBookId id) {
        LikedBookEntity likedBookEntity = likedBookMapper.toEntity(dto);
        LikedBookEntity updatedLikedBook = iJpaLikedBookRepository.save(likedBookEntity);
        return likedBookMapper.toDomain(updatedLikedBook);
    }

    @Override
    public int countByBookId(Long bookId) {
        return iJpaLikedBookRepository.countByBookId(bookId);
    }

    @Override
    public List<Book> getLikedBooksByUserId(Long userId) {
        return iJpaLikedBookRepository.getLikedBooksByUserId(userId)
                .stream()
                .map(bookPersistenceMapper::toDomain)
                .toList();
    }
}
