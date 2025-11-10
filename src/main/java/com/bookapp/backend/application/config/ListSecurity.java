package com.bookapp.backend.application.config;

import com.bookapp.backend.domain.ports.in.IListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("listSecurity")
@RequiredArgsConstructor
public class ListSecurity {
    private final IListService listService;
    public boolean isListOwner(Long listId, Long userId){
        return listService
                .getById(listId)
                .map(list -> list.getUser().getId().equals(userId))
                .orElse(false);
    }
}
