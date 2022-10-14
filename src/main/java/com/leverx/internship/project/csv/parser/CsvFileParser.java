package com.leverx.internship.project.csv.parser;

import com.leverx.internship.project.csv.model.CsvUser;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class CsvFileParser {

  public List<CsvUser> getUsersFromCsv(MultipartFile csvFile) {
    try (Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
      return new CsvToBeanBuilder<CsvUser>(reader)
          .withSeparator(',')
          .withType(CsvUser.class)
          .withIgnoreLeadingWhiteSpace(true)
          .withIgnoreEmptyLine(true)
          .build()
          .parse();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

}
