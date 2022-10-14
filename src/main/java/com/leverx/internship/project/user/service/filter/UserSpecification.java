package com.leverx.internship.project.user.service.filter;

import com.leverx.internship.project.user.repository.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

  private UserSpecification() {
  }

  public static Specification<User> userParamHasEmail(String email) {
    return (user, criteriaQuery, criteriaBuilder) -> {
      if (email != null) {
        return criteriaBuilder.equal(user.get("email"), email);
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<User> userParamHasFirstName(String firstName) {
    return (user, criteriaQuery, criteriaBuilder) -> {
      if (firstName != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(user.get("firstName")), "%" + firstName.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<User> userParamHasLastName(String lastName) {
    return (user, criteriaQuery, criteriaBuilder) -> {
      if (lastName != null) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(user.get("lastName")), "%" + lastName.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }
}