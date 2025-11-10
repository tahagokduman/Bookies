package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.UserController;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebMapper;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

 @InjectMocks
 private UserController userController;

 @Mock
 private IUserService userService;

 @Mock
 private UserWebMapper userWebMapper;

 private AutoCloseable closeable;

 @BeforeEach
 void setUp() {
  closeable = MockitoAnnotations.openMocks(this);
 }

 @Test
 void getUserById_returnsUser_whenFound() {
  Long userId = 1L;
  User user = createMockUser(userId);
  UserResponseDTO responseDTO = createMockResponseDTO(userId);

  when(userService.getById(userId)).thenReturn(Optional.of(user));
  when(userWebMapper.toResponseDTO(user)).thenReturn(responseDTO);

  ResponseEntity<UserResponseDTO> response = userController.getUserById(userId);

  assertThat(response.getStatusCodeValue()).isEqualTo(200);
  assertThat(response.getBody()).isEqualTo(responseDTO);
 }

 @Test
 void getUserById_throws_whenNotFound() {
  Long userId = 99L;
  when(userService.getById(userId)).thenReturn(Optional.empty());

  assertThatThrownBy(() -> userController.getUserById(userId))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("User with id " + userId + " not found");
 }

 @Test
 void updateUserById_returnsUpdatedUser() {
  Long userId = 1L;
  UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
  updateDTO.setUsername("newUser");
  updateDTO.setEmail("new@example.com");
  updateDTO.setPassword("newpass123");

  User domainUser = createMockUser(userId);
  User updatedUser = createMockUser(userId);
  updatedUser.setUsername("newUser");

  UserResponseDTO responseDTO = createMockResponseDTO(userId);
  responseDTO.setUsername("newUser");

  when(userWebMapper.updateRequestDTOtoDomain(updateDTO)).thenReturn(domainUser);
  when(userService.update(userId, domainUser)).thenReturn(updatedUser);
  when(userWebMapper.toResponseDTO(updatedUser)).thenReturn(responseDTO);

  ResponseEntity<UserResponseDTO> response = userController.updateUserById(userId, updateDTO);

  assertThat(response.getStatusCodeValue()).isEqualTo(200);
  assertThat(response.getBody().getUsername()).isEqualTo("newUser");
 }

 @Test
 void deleteUserById_success_whenExists() {
  Long userId = 1L;
  when(userService.getById(userId)).thenReturn(Optional.of(createMockUser(userId)));

  ResponseEntity<?> response = userController.deleteUserById(userId);

  assertThat(response.getStatusCodeValue()).isEqualTo(204);
  verify(userService, times(1)).delete(userId);
 }

 @Test
 void deleteUserById_throws_whenNotFound() {
  Long userId = 999L;
  when(userService.getById(userId)).thenReturn(Optional.empty());

  assertThatThrownBy(() -> userController.deleteUserById(userId))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("User with id " + userId + " not found");
 }

 @Test
 void getUsersPaging_returnsPagedUsers() {
  int page = 0, size = 2;
  List<User> users = List.of(createMockUser(1L), createMockUser(2L));
  Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

  when(userService.getUsersPaging(page, size)).thenReturn(userPage);
  when(userWebMapper.toResponseDTO(any(User.class)))
          .thenAnswer(invocation -> createMockResponseDTO(((User) invocation.getArgument(0)).getId()));

  ResponseEntity<?> response = userController.getUsersPaging(page, size);

  assertThat(response.getStatusCodeValue()).isEqualTo(200);
 }

 @Test
 void searchUser_returnsResults() {
  String keyword = "user";
  int page = 0, size = 2;
  List<User> users = List.of(createMockUser(1L), createMockUser(2L));
  Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

  when(userService.searchUser(keyword, page, size)).thenReturn(userPage);
  when(userWebMapper.toResponseDTO(any(User.class)))
          .thenAnswer(invocation -> createMockResponseDTO(((User) invocation.getArgument(0)).getId()));

  ResponseEntity<?> response = userController.searchUser(keyword, page, size);

  assertThat(response.getStatusCodeValue()).isEqualTo(200);
 }

 private User createMockUser(Long id) {
  User user = new User();
  user.setId(id);
  user.setUsername("user" + id);
  user.setEmail("user" + id + "@example.com");
  user.setBirthdayDate(LocalDate.of(2000, 1, 1));
  user.setFollowersCount(new NonNegativeInteger(5));
  user.setFollowingCount(new NonNegativeInteger(3));
  return user;
 }

 private UserResponseDTO createMockResponseDTO(Long id) {
  return UserResponseDTO.builder()
          .id(id)
          .username("user" + id)
          .email("user" + id + "@example.com")
          .birthdayDate(LocalDate.of(2000, 1, 1))
          .followersCount(new NonNegativeInteger(5))
          .followingCount(new NonNegativeInteger(3))
          .build();
 }
}
