package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.follow.ListFollowCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.ListFollowResponseDTO;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ListFollowWebMapper {

    private final UserWebMapper userMapper;
    private final ListWebMapper listMapper;

    public ListFollow toDomain(ListFollowCreateRequestDTO dto) {
        ListFollow listFollow = new ListFollow();
        listFollow.setId(new ListFollowId(dto.getUserId(), dto.getListId()));
        return listFollow;
    }

    public ListFollowResponseDTO toResponseDto(ListFollow entity) {
        ListFollowResponseDTO dto = new ListFollowResponseDTO();
        dto.setUserId(entity.getId().getUserId());
        dto.setListId(entity.getId().getListId());
        dto.setList(listMapper.toResponseDto(entity.getList()));
        dto.setUser(userMapper.toResponseDTO(entity.getUser()));
        return dto;
    }
}

