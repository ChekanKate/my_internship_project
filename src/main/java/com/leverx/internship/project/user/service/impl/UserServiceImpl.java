package com.leverx.internship.project.user.service.impl;

import com.leverx.internship.project.exception.JwtAuthenticationException;
import com.leverx.internship.project.project.repository.ProjectRepository;
import com.leverx.internship.project.project.repository.entity.Project;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.repository.UserRepository;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.service.UserService;
import com.leverx.internship.project.user.service.filter.UserSpecification;
import com.leverx.internship.project.user.web.converter.UserConverter;
import com.leverx.internship.project.user.web.dto.request.UserBodyRequest;
import com.leverx.internship.project.user.web.dto.request.UserParamRequest;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserConverter userConverter;
  private final ProjectRepository projectRepository;
  private static final int LIMIT_OF_PROJECTS = 2;
  private final Instant dateNow;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
      UserConverter userConverter,
      ProjectRepository projectRepository, Clock clock) {
    this.userRepository = userRepository;
    this.userConverter = userConverter;
    this.projectRepository = projectRepository;
    this.dateNow = clock.instant();
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserResponse> findAll(int page, int size, UserParamRequest params) {
    Specification<User> spec =
        Specification.where(
            UserSpecification.userParamHasEmail(params.getEmail())
                .and(UserSpecification.userParamHasFirstName(params.getFirstName()))
                .and(UserSpecification.userParamHasLastName(params.getLastName())));
    Pageable paging = PageRequest.of(page, size);
    Page<User> userPage = userRepository.findAll(spec, paging);
    return userConverter.userListToUserResponseList(userPage.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findByEmail(String email) {
    return userConverter.toUserResponse(
        userRepository
            .findUserByEmail(email)
            .orElseThrow(() -> new NotFoundException("Such user doesn't exist")));
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findById(Integer id) {
    User user = getUser(id);
    return userConverter.toUserResponse(user);
  }

  @Transactional
  @Override
  public UserResponse create(UserBodyRequest userDto) {
    User user = userConverter.toEntity(userDto);
    user.setRole(Role.valueOf(userDto.getRole().toString()));
    return userConverter.toUserResponse(userRepository.save(user));
  }

  @Transactional
  @Override
  public UserResponse update(Integer id, UserBodyRequest userBodyRequest) {
    User user = getUser(id);
    User newUser =
        userConverter.toEntity(userConverter.toUpdatedUserResponse(userBodyRequest, user));
    newUser.setCreatedAt(user.getCreatedAt());
    newUser.setId(user.getId());
    return userConverter.toUserResponse(userRepository.save(newUser));
  }

  @Transactional
  @Override
  public void delete(Integer id) {
    getUser(id);
    userRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDetails getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return Optional.of(((UserDetails) principal))
        .orElseThrow(() -> new JwtAuthenticationException("Authentication exception"));
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findUserByEmail(String email) {
    return userConverter.toUserResponse(
        userRepository
            .findUserByEmail(email)
            .orElseThrow(
                () -> new NotFoundException("User with email: " + email + " doesn't exist")));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> findAvailableEmployee() {
    List<UserResponse> userResponseList =
        userConverter.userListToUserResponseList(
            userRepository.findAll().stream().filter(User::isActive).collect(Collectors.toList()));
    List<UserResponse> availableEmployeeList = new ArrayList<>();
    userResponseList.forEach(
        userResponse -> {
          List<Project> projects =
              new ArrayList<>(
                  projectRepository.findProjectsByEmployeesIdAndDateEndAfter(
                      userResponse.getId(), dateNow));
          if (projects.size() < LIMIT_OF_PROJECTS) {
            availableEmployeeList.add(userResponse);
          }
        });
    return availableEmployeeList;
  }

  private User getUser(Integer id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User with id: " + id + " doesn't exist"));
  }
}