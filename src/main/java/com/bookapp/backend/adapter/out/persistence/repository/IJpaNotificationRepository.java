package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("select u from NotificationEntity u where u.receiverId = :receiverId")
    List<NotificationEntity> findAllByReceiverId(@Param("receiverId") Long receiverId);

    @Query("select count(n) FROM NotificationEntity n where n.isRead = false")
    Long findCountOfDidntReadNotifications();
}
