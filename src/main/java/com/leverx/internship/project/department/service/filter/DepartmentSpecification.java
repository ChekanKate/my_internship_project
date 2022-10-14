package com.leverx.internship.project.department.service.filter;

import com.leverx.internship.project.department.repository.entity.Department;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {
  private DepartmentSpecification() {}

  public static Specification<Department> departmentParamHasDescription(String description) {
    return (department, criteriaQuery, criteriaBuilder) -> {
      if (description != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(department.get("description")),
            "%" + description.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<Department> departmentParamHasName(String name) {
    return (department, criteriaQuery, criteriaBuilder) -> {
      if (name != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(department.get("name")),
            "%" + name.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }
}
