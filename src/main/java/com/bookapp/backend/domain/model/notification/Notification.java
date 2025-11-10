package com.bookapp.backend.domain.model.notification;

import com.bookapp.backend.domain.model.base.BaseModel;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseModel {

    private Long senderId;
    private Long receiverId;
    private NotificationType type;
    private String targetId;
    private boolean isRead;
}
