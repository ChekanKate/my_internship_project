package com.leverx.internship.project.user.web.converter;

import com.leverx.internship.project.csv.model.CsvUser;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.web.dto.request.UserBodyRequest;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverter {

  private final ModelMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final Clock clock;

  public User toEntity(@NonNull UserBodyRequest userBodyRequest) {
    mapper.typeMap(UserBodyRequest.class, User.class)
        .addMappings(mapper -> mapper.skip(User::setPassword))
        .setPostConverter(specifiedDataConverter());
    return mapper.map(userBodyRequest, User.class);
  }

  public User toEntity(@NonNull UserResponse userResponse) {
    return mapper.map(userResponse, User.class);
  }

  public UserResponse toUpdatedUserResponse(@NonNull UserBodyRequest userReqToUpdate, User user) {
    UserResponse userResponse = toUserResponse(user);
    mapper.map(userReqToUpdate, userResponse);
    return userResponse;
  }

  public User fromCsvToEntity(@NonNull CsvUser csvUser) {
    mapper.typeMap(CsvUser.class, User.class)
        .setPostConverter(csvUserSpecifiedDataConverter());
    return mapper.map(csvUser, User.class);
  }

  public UserResponse toUserResponse(@NonNull User user) {
    return mapper.map(user, UserResponse.class);
  }

  public List<UserResponse> userListToUserResponseList(List<User> users) {
    if (users != null) {
      List<UserResponse> userResponseList = new ArrayList<>();
      users.forEach(user -> userResponseList.add(toUserResponse(user)));
      return userResponseList;
    } else {
      return new ArrayList<>();
    }
  }

  public List<User> usersRespListToUserList(List<UserResponse> usersResponse) {
    if (usersResponse != null) {
      List<User> users = new ArrayList<>();
      usersResponse.forEach(userResponse -> users.add(toEntity(userResponse)));
      return users;
    } else {
      return new ArrayList<>();
    }
  }

  public Set<UserResponse> usersSetToUsersResponseSet(Set<User> users) {
    if (users != null) {
      Set<UserResponse> usersResponse = new HashSet<>();
      users.forEach(user -> usersResponse.add(toUserResponse(user)));
      return usersResponse;
    } else {
      return new HashSet<>();
    }
  }

  public Set<User> usersResponseSetToUserSet(Set<UserResponse> userResponseSet) {
    if (userResponseSet != null) {
      Set<User> users = new HashSet<>();
      userResponseSet.forEach(userResponse -> users.add(toEntity(userResponse)));
      return users;
    } else {
      return new HashSet<>();
    }
  }

  public Converter<UserBodyRequest, User> specifiedDataConverter() {
    return mappingContext -> {
      UserBodyRequest source = mappingContext.getSource();
      User destination = mappingContext.getDestination();
      destination.setPassword(passwordEncoder.encode(source.getPassword()));
      return mappingContext.getDestination();
    };
  }

  public Converter<CsvUser, User> csvUserSpecifiedDataConverter() {
    return mappingContext -> {
      CsvUser source = mappingContext.getSource();
      User destination = mappingContext.getDestination();
      destination.setPassword(passwordEncoder.encode(source.getPassword()));
      return mappingContext.getDestination();
    };
  }
}