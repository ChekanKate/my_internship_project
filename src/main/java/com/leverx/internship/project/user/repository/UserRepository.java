package com.leverx.internship.project.user.repository;

import com.leverx.internship.project.user.repository.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
  Optional<User> findUserByEmail(String email);
}
