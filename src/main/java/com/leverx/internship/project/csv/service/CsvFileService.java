package com.leverx.internship.project.csv.service;

import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CsvFileService {
  List<UserResponse> uploadUsers(MultipartFile csvFile);
}
