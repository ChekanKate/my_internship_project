package com.leverx.internship.project.csv.web.controller;

import com.leverx.internship.project.csv.service.CsvFileService;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CsvFileUploadController {

  private final CsvFileService csvFileService;

  @PostMapping(value = "/upload")
  public List<UserResponse> uploadUsersFromCsv(@RequestBody MultipartFile csvFile) {
    return csvFileService.uploadUsers(csvFile);
  }
}
