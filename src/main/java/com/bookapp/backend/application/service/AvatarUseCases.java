package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.in.IAvatarService;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvatarUseCases implements IAvatarService {

    private final IAvatarRepository avatarRepository;
    private final IUserRepository userRepository;

    @Override
    public List<Avatar> getAll() {
        return avatarRepository.findAll();
    }

    @Override
    public Optional<Avatar> getById(Long id) {
        return Optional.ofNullable(avatarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avatar with id " + id + " was not found")));
    }

    @Override
    public Avatar create(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    @Override
    public void delete(Long id) {
        avatarRepository.deleteById(id);
    }

    @Override
    public Avatar update(Long id, Avatar avatar) {
        return avatarRepository.update(avatar, id);
    }


}
