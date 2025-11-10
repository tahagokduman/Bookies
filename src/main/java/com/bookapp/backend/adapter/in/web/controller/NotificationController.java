package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.response.notification.NotificationResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.NotificationWebMapper;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;
    private final NotificationWebMapper mapper;

    @GetMapping
    public CollectionModel<NotificationResponseDTO> getMyNotifications(
            @AuthenticationPrincipal(expression = "id") Long userId) {

        List<Notification> notifications = notificationService.findAllByReceiverId(userId);

        List<NotificationResponseDTO> dtoList = notifications.stream()
                .map(mapper::toResponseDTO)
                .peek(dto -> dto.add(
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(NotificationController.class)
                                        .getMyNotifications(userId)
                        ).withSelfRel()
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(dtoList);
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal(expression = "id") Long userId) {
        return ResponseEntity.ok(notificationService.findCountOfDidntReadNotifications());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        notificationService.deleteNotification(id, user.getId());
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
