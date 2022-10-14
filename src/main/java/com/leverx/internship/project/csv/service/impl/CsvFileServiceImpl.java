package com.leverx.internship.project.csv.service.impl;

import com.leverx.internship.project.csv.model.CsvUser;
import com.leverx.internship.project.csv.parser.CsvFileParser;
import com.leverx.internship.project.csv.service.CsvFileService;
import com.leverx.internship.project.user.repository.UserRepository;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.web.converter.UserConverter;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class CsvFileServiceImpl implements CsvFileService {

  private final CsvFileParser csvFileParser;
  private final UserRepository userRepository;
  private final UserConverter userConverter;

  @Override
  public List<UserResponse> uploadUsers(MultipartFile csvFile) {
    List<CsvUser> csvUsers = csvFileParser.getUsersFromCsv(csvFile);
    List<User> users = new ArrayList<>();
    csvUsers.forEach(csvUser -> users.add(userConverter.fromCsvToEntity(csvUser)));
    List<User> uploadedUsers = userRepository.saveAll(users);
    return userConverter.userListToUserResponseList(uploadedUsers);
  }
}
