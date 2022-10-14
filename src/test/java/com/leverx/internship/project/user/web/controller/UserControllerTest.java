package com.leverx.internship.project.user.web.controller;

import com.leverx.internship.project.config.H2ConfigTest;
import com.leverx.internship.project.config.SpringConfig;
import com.leverx.internship.project.config.WebConfig;
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
public class UserControllerTest {

  private final WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Autowired
  public UserControllerTest(WebApplicationContext webApplicationContext) {
    this.webApplicationContext = webApplicationContext;
  }

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void getAllUsersByFirstNameFieldTestPositive_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/users")
                .param("size", "3")
                .param("page", "0")
                .param("firstName", "User2")
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void findUserByIdTestPositive_ShouldReturnOk() throws Exception {
    MockHttpServletResponse response = mockMvc
        .perform(
            get("/users/{id}", 2)
                .param("size", "3")
                .param("page", "0")
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void createTestPositive_ShouldReturnCreated() throws Exception {
    String jsonString =
        new JSONObject()
            .put("firstName", "User4")
            .put("lastName", "User4")
            .put("password", "password")
            .put("email", "mail4@mail.ru")
            .put("role", "ADMIN")
            .put("is_active", "true")
            .toString();
    MockHttpServletResponse response = mockMvc
        .perform(
            post("/users")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void updateTestPositive_ShouldReturnOk() throws Exception {
    String jsonString =
        new JSONObject()
            .put("firstName", "UserUpdated").toString();
    final MockHttpServletResponse response = mockMvc.perform(put("/users/{id}", 2)
            .content(jsonString).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @WithMockUser(value = "mail@mail.ru")
  void deleteTestPositive_ShouldReturnNoContent() throws Exception {
    final MockHttpServletResponse response = mockMvc.perform(delete("/users/{id}", 4)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }
}