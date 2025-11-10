package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.NotificationEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.NotificationPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaNotificationRepository;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.ports.out.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements INotificationRepository {

    private final IJpaNotificationRepository jpaRepository;
    private final NotificationPersistenceMapper mapper;

    @Override
    public List<Notification> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Notification update(Notification notification, Long id) {
        notification.setId(id);
        NotificationEntity entity = mapper.toEntity(notification);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Notification> findAllByReceiverId(Long receiverId) {
        return jpaRepository.findAllByReceiverId(receiverId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long findCountOfDidntReadNotifications() {
        return jpaRepository.findCountOfDidntReadNotifications();
    }
}
