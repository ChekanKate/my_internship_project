package com.leverx.internship.project.report.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.leverx.internship.project.config.H2ConfigTest;
import com.leverx.internship.project.config.SpringConfig;
import com.leverx.internship.project.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {SpringConfig.class, WebConfig.class, H2ConfigTest.class})
@WebAppConfiguration(value = "src/main/java/com/leverx/internship/project")
@Sql(value = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:delete_data-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Scope(value = "test")
class ReportControllerTest {

  private final WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  public static final String EXCEL_XLSX_CONTENT_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  @Autowired
  public ReportControllerTest(WebApplicationContext webApplicationContext) {
    this.webApplicationContext = webApplicationContext;
  }

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  void testDownloadWorkloadReport_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/report/workload")
                .accept(EXCEL_XLSX_CONTENT_TYPE))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }


  @Test
  void testDownloadAvailableEmployeeReport_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/report/available")
                .accept(EXCEL_XLSX_CONTENT_TYPE))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
