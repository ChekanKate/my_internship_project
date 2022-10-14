package com.leverx.internship.project.project.service.filter;

import com.leverx.internship.project.project.repository.entity.Project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
  private ProjectSpecification() {
    throw new UnsupportedOperationException("Class instance can't be instantiated.");
  }

  public static Specification<Project> projectParamHasDescription(String description) {
    return (project, criteriaQuery, criteriaBuilder) -> {
      if (description != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(project.get("description")),
            "%" + description.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<Project> projectParamHasName(String name) {
    return (project, criteriaQuery, criteriaBuilder) -> {
      if (name != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(project.get("name")), "%" + name.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }
}
