package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaUserRepository;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements IUserRepository {

    @Autowired
    private IJpaUserRepository iJpaUserRepository;

    @Autowired
    private UserPersistenceMapper userPersistenceMapper;

    @Override
    public List<User> findAll() {
        return iJpaUserRepository
                .findAll()
                .stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return iJpaUserRepository.findById(aLong).map(userPersistenceMapper::toDomain);
    }
    @Override
    public void deleteAll() {
        iJpaUserRepository.deleteAll();
    }


    @Override
    public User save(User entity) {
        UserEntity userEntity = userPersistenceMapper.toEntity(entity);
        UserEntity savedEntity = iJpaUserRepository.save(userEntity);
        return userPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long aLong) {
        iJpaUserRepository.deleteById(aLong);
    }

    @Override
    public User update(User dto, Long aLong) {
        UserEntity user = userPersistenceMapper.toEntity(dto);
        UserEntity updatedUser = iJpaUserRepository.save(user);
        return userPersistenceMapper.toDomain(updatedUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return iJpaUserRepository.findByUsername(username).map(userPersistenceMapper::toDomain);
    }
    public Optional<User> findByEmail(String email){
        return iJpaUserRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }
    private Page<User> convertToDomainPage(Page<UserEntity> userEntityPage) {
        List<User> bookList = userEntityPage.getContent()
                .stream()
                .map(userPersistenceMapper::toDomain)
                .toList();

        Pageable pageable = userEntityPage.getPageable();
        return new PageImpl<>(bookList, pageable, userEntityPage.getTotalElements());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return convertToDomainPage(iJpaUserRepository.findAll(pageable));
    }

    @Override
    public Page<User> searchUser(String username, Pageable pageable) {
        return convertToDomainPage(iJpaUserRepository.findByTitleContainingIgnoreCase(username, pageable));
    }
}
