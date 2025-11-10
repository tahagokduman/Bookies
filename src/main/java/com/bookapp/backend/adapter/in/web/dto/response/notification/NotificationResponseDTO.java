package com.bookapp.backend.adapter.in.web.dto.response.notification;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationResponseDTO extends BaseResponseDTO<NotificationResponseDTO> {

    private Long senderId;
    private Long receiverId;
    private String type;
    private String targetId;
    private boolean isRead;
}
