package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IUserService;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserUseCases implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.of(
                userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " was not found"))
        );
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        Long jwtUserId = userDetails.getId();

        if (!jwtUserId.equals(id)) {
            throw new AccessDeniedException("You are not allowed to modify this user.");
        }
        ensureUserExists(id);
        userRepository.deleteById(id);
    }

    @Override
    public User update(Long id, User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        Long jwtUserId = userDetails.getId();

        if (!jwtUserId.equals(id)) {
            throw new AccessDeniedException("You are not allowed to modify this user.");
        }

        ensureUserExists(id);
        return userRepository.update(user, id);
    }

    private void ensureUserExists(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("User with id " + id + " was not found");
        }
    }

    @Override
    public Page<User> getUsersPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchUser(username, pageable);
    }
}
