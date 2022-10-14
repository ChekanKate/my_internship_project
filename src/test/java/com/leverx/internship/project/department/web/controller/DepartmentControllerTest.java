package com.leverx.internship.project.department.web.controller;

import com.leverx.internship.project.config.H2ConfigTest;
import com.leverx.internship.project.config.SpringConfig;
import com.leverx.internship.project.config.WebConfig;
import com.leverx.internship.project.user.repository.entity.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {SpringConfig.class, WebConfig.class, H2ConfigTest.class})
@WebAppConfiguration(value = "src/main/java/com/leverx/internship/project")
@Sql(value = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:delete_data-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Scope(value = "test")
public class DepartmentControllerTest {

  private final WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Autowired
  public DepartmentControllerTest(WebApplicationContext webApplicationContext) {
    this.webApplicationContext = webApplicationContext;
  }

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void getAllDepartmentsTest_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/departments")
                .param("size", "3")
                .param("page", "0")
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void findDepartmentByIdTest_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/departments/{id}", 2)
                .param("size", "3")
                .param("page", "0")
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void createTest_ShouldReturnCreated() throws Exception {
    Optional<User> userOptional = Optional.of(new User());
    userOptional.get().setEmail("mail@mail.ru");
    String jsonString =
        new JSONObject()
            .put("name", "name1")
            .put("description", "description")
            .toString();
    MockHttpServletResponse response = mockMvc
        .perform(
            post("/departments")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void updateTest_ShouldReturnOk() throws Exception {
    Optional<User> userOptional = Optional.of(new User());
    userOptional.get().setEmail("mail@mail.ru");
    String jsonString =
        new JSONObject()
            .put("description", "new description").toString();
    final MockHttpServletResponse response = mockMvc.perform(put("/departments/{id}", 2)
            .content(jsonString).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void deleteTest_ShouldReturnNoContent() throws Exception {
    final MockHttpServletResponse response = mockMvc.perform(delete("/departments/{id}", 4)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus()) ;
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void addUserToDepartmentTest_ShouldReturnOk() throws Exception {
    Optional<User> userOptional = Optional.of(new User());
    userOptional.get().setEmail("mail@mail.ru");
    final MockHttpServletResponse response =
        mockMvc
            .perform(
                put("/departments/{idDep}/users/{idUser}", 4, 2)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void addProjectToDepartmentTest_ShouldReturnCreated() throws Exception {
    Optional<User> userOptional = Optional.of(new User());
    userOptional.get().setEmail("mail1@mail.com");
    final MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/departments/{idDep}/projects/{idProj}", 4, 2)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail1@mail.com")
  void deleteProjectFromDepartmentTest_ShouldReturnNoContent() throws Exception {
    Optional<User> userOptional = Optional.of(new User());
    userOptional.get().setEmail("mail1@mail.com");
    final MockHttpServletResponse response = mockMvc.perform(delete("/departments/{idDep}/projects/{idProj}", 3, 4)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }
}
