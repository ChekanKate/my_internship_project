package com.leverx.internship.project.department.web.controller;

import com.leverx.internship.project.department.service.DepartmentService;
import com.leverx.internship.project.department.web.dto.request.DepartmentBodyRequest;
import com.leverx.internship.project.department.web.dto.request.DepartmentParamRequest;
import com.leverx.internship.project.department.web.dto.response.DepartmentProjectsResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentUsersResponse;
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
@RequestMapping("/departments")
@AllArgsConstructor
public class DepartmentController {

  private final DepartmentService departmentService;

  @GetMapping
  @Operation(summary = "Get List of all departments with pagination from the database")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Found departments",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content)
      })
  @ResponseStatus(HttpStatus.OK)
  public List<DepartmentResponse> getDepartments(
      @Parameter(description = "Number of departments shown", example = "size=1")
      @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "Page number", example = "page=1")
      @RequestParam(name = "size", defaultValue = "3")
          int size,
      @Parameter(description = "Name of department", example = "name=Java")
      @RequestParam(name = "name", required = false) String name,
      @Parameter(description = "Description of department")
      @RequestParam(name = "description", required = false) String description) {
    return departmentService.findAll(page, size, new DepartmentParamRequest(name, description));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get department by id from the database")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Found the department",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content)
      })
  @ResponseStatus(HttpStatus.OK)
  public DepartmentResponse getDepartment(
      @Parameter(description = "Required department id", example = "1") @PathVariable("id")
          int id) {
    return departmentService.findById(id);
  }

  @PostMapping
  @Operation(summary = "Save department to the database")
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
  public DepartmentResponse create(
      @Parameter(description = "Department information")
      @RequestBody DepartmentBodyRequest departmentBodyRequest) {
    return departmentService.create(departmentBodyRequest);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update department in the database")
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
  public DepartmentResponse update(
      @Parameter(description = "Department information for update") @RequestBody
          DepartmentBodyRequest departmentBodyRequestToUpdate,
      @Parameter(description = "Required department id", example = "1") @PathVariable("id")
          int id) {
    return departmentService.update(id, departmentBodyRequestToUpdate);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete department in the database")
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
      @Parameter(description = "Required department id", example = "1") @PathVariable("id")
          int id) {
    departmentService.delete(id);
  }

  @Operation(summary = "Add user to department")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Ok"),
          @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content)
      })
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{idDep}/users/{idUser}")
  public DepartmentUsersResponse addUserToDep(
      @Parameter(description = "Required department id", example = "1") @PathVariable("idDep")
          int idDep,
      @Parameter(description = "Required user id", example = "1") @PathVariable("idUser")
          int idUser) {
    return departmentService.addEmployeeToDepartment(idDep, idUser);
  }

  @PostMapping("/{idDep}/projects/{idProject}")
  @Operation(summary = "Add project to department")
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
  public DepartmentProjectsResponse addProjectToDep(
      @Parameter(description = "Required department id", example = "1")
      @PathVariable("idDep") int idDep,
      @Parameter(description = "Required project id", example = "1")
      @PathVariable("idProject") int idProject) {
    return departmentService.addProjectToDepartment(idDep, idProject);
  }

  @DeleteMapping("/{idDep}/projects/{idProject}")
  @Operation(summary = "Delete project from department")
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
  public void deleteProjectToDep(@PathVariable("idDep") int idDep,
      @PathVariable("idProject") int idProject) {
    departmentService.removeProjectFromDepartment(idDep, idProject);
  }

  @GetMapping("/{idDep}/users")
  @Operation(summary = "Get List of all users in department")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Found users",
              content = @Content(mediaType = "application/json")),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content)
      })
  public DepartmentUsersResponse findAllUsersInDepartment(
      @Parameter(description = "Required department id") @PathVariable("idDep") int idDep) {
    return departmentService.findAllUsersInDepartment(idDep);
  }

  @GetMapping("/{idDep}/projects")
  @Operation(summary = "Get List of all users in department")
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
  public DepartmentProjectsResponse findAllProjectsInDepartment(
      @Parameter(description = "Required department id") @PathVariable("idDep") int idDep) {
    return departmentService.findAllProjectsInDepartment(idDep);
  }
}