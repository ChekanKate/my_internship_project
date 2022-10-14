package com.leverx.internship.project.project.web.controller;

import com.leverx.internship.project.project.service.ProjectService;
import com.leverx.internship.project.project.web.dto.request.ProjectBodyRequest;
import com.leverx.internship.project.project.web.dto.request.ProjectParamRequest;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.project.web.dto.response.ProjectUsersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("projects")
@AllArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get List of all projects with pagination from the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found projects",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  public List<ProjectResponse> getProjects(
      @Parameter(description = "Number of projects shown", example = "size=1")
          @RequestParam(required = false, defaultValue = "3")
          int size,
      @Parameter(description = "Page number", example = "page=1")
          @RequestParam(required = false, defaultValue = "0")
          int page,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String description) {
    return projectService.findAll(page, size, new ProjectParamRequest(description, name));
  }

  @GetMapping("/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get List of all projects with employees from the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found projects",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  public ProjectUsersResponse getProjectUsers(
      @Parameter(description = "Id of required project", example = "1") @PathVariable("id")
          int id) {
    return projectService.findProjectUsers(id);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get project by id from the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the project",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.OK)
  public ProjectResponse getProject(
      @Parameter(description = "Required project id", example = "1") @PathVariable("id") int id) {
    return projectService.findById(id);
  }

  @PostMapping
  @Operation(summary = "Save project to the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "203",
            description = "Created",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectResponse create(
      @Parameter(description = "Project information") @RequestBody
          ProjectBodyRequest projectBodyRequest) {
    return projectService.create(projectBodyRequest);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update project in the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ok",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.OK)
  public ProjectResponse update(
      @Parameter(description = "Project information for update") @RequestBody
          ProjectBodyRequest projectBodyRequestToUpdate,
      @Parameter(description = "Required project id", example = "1") @PathVariable("id") int id) {
    return projectService.update(id, projectBodyRequestToUpdate);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete project in the database")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "No content"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @Parameter(description = "Required project id", example = "1") @PathVariable("id") int id) {
    projectService.delete(id);
  }

  @PostMapping("/{idProject}/users/{idUser}")
  @Operation(summary = "Add user to the project in the database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "203",
            description = "Created",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectUsersResponse addUserToProject(
      @Parameter(description = "Required project id", example = "1") @PathVariable("idProject")
          int idProject,
      @Parameter(description = "Required user id", example = "1") @PathVariable("idUser")
          int idUser) {
    return projectService.addUserToProject(idProject, idUser);
  }

  @DeleteMapping("/{idProject}/users/{idUser}")
  @Operation(summary = "Remove user from the project in the database")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "No content"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content)
      })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserFromProject(
      @Parameter(description = "Required project id") @PathVariable("idProject") int idProject,
      @Parameter(description = "Required user id") @PathVariable("idUser") int idUser) {
    projectService.deleteUserFromProject(idProject, idUser);
  }
}