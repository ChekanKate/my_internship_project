package com.leverx.internship.project.user.service.impl;

import com.leverx.internship.project.project.repository.ProjectRepository;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.repository.UserRepository;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.service.UserService;
import com.leverx.internship.project.user.web.converter.UserConverter;
import com.leverx.internship.project.user.web.dto.request.UserBodyRequest;
import com.leverx.internship.project.user.web.dto.request.UserParamRequest;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  private UserService userServiceUnderTest;
  @Mock private UserConverter userConverter;
  @Mock private ProjectRepository projectRepository;
  @Mock private Clock clock;

  @BeforeEach
  public void init() {
    userServiceUnderTest = new UserServiceImpl(userRepository, userConverter, projectRepository, clock) {};
  }

  @Test
  void findAllTest_ShouldReturnList() {
    UserResponse userDto =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    List<UserResponse> expected = new ArrayList<>();
    expected.add(userDto);
    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    final Page<User> users = new PageImpl<>(List.of(user));
    when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(users);

    UserResponse userDto1 =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    List<UserResponse> usersDto = List.of(userDto1);
    when(userConverter.userListToUserResponseList(users.toList())).thenReturn(usersDto);

    List<UserResponse> actual =
        userServiceUnderTest.findAll(0, 3, new UserParamRequest("Alex", null, null));

    assertEquals(expected.size(), actual.size());
  }

  @Test
  void saveUserTest_ShouldCreate() {
    UserResponse expected =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);

    UserBodyRequest userDto =
        new UserBodyRequest(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);

    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    when(userConverter.toEntity(userDto)).thenReturn(user);

    final UserResponse userDto1 =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    when(userConverter.toUserResponse(any(User.class))).thenReturn(userDto1);

    User user1 = new User();
    user1.setId(1);
    user1.setFirstName("Ivan");
    user1.setLastName("Ivanov");
    user1.setRole(Role.ADMIN);
    user1.setActive(true);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    when(userRepository.save(any(User.class))).thenReturn(user1);

    final UserResponse result = userServiceUnderTest.create(userDto);

    assertEquals(expected, result);
  }

  @Test
  void findByIdTest_ShouldReturnUser() {
    UserResponse expected =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    final Optional<User> userOptional = Optional.of(user);
    when(userRepository.findById(1)).thenReturn(userOptional);
    UserResponse userDto =
        new UserResponse(1, "mail1@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    when(userConverter.toUserResponse(any(User.class))).thenReturn(userDto);

    // Run the test
    final UserResponse result = userServiceUnderTest.findById(1);

    assertEquals(expected, result);
  }

  @Test
  void updateUserTest_ShouldUpdate() {
    final UserResponse expected =
        new UserResponse(1, "mail1@mail.ru", "password", "Alex", "Ivanov", Role.ADMIN, true);
    final UserBodyRequest userDtoUpdate = new UserBodyRequest();
    userDtoUpdate.setFirstName("Alex");
    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setEmail("email");
    user.setPassword("password");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    final Optional<User> userOptional = Optional.of(user);
    when(userRepository.findById(1)).thenReturn(userOptional);

    User user1 = new User();
    user1.setId(1);
    user1.setFirstName("Ivan");
    user1.setLastName("Ivanov");
    user1.setEmail("email");
    user1.setPassword("password");
    user1.setRole(Role.ADMIN);
    user1.setActive(true);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    when(userConverter.toEntity(any(UserResponse.class))).thenReturn(user1);

    final UserResponse userDto =
        new UserResponse(1, "mail1@mail.ru", "password", "Alex", "Ivanov", Role.ADMIN, true);
    when(userConverter.toUpdatedUserResponse(eq(userDtoUpdate), any(User.class)))
        .thenReturn(userDto);
    when(userConverter.toUserResponse(any(User.class))).thenReturn(userDto);
    User user2 = new User();
    user2.setId(1);
    user2.setPassword("password");
    user2.setEmail("email");
    user2.setFirstName("Alex");
    user2.setLastName("Ivanov");
    user2.setRole(Role.ADMIN);
    user2.setActive(true);
    user2.setCreatedAt(clock.instant());
    user2.setUpdatedAt(clock.instant());
    when(userRepository.save(any(User.class))).thenReturn(user2);

    final UserResponse result = userServiceUnderTest.update(1, userDtoUpdate);

    assertEquals(expected, result);
  }

  @Test
  void deleteTest_ShouldDelete() {
    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    final Optional<User> userOptional = Optional.of(user);
    when(userRepository.findById(1)).thenReturn(userOptional);

    userServiceUnderTest.delete(1);
  }
}
