package com.leverx.internship.project.department.repository;

import com.leverx.internship.project.department.repository.entity.Department;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends JpaRepository<Department, Integer>,
    JpaSpecificationExecutor<Department> {
  Optional<Department> findDepartmentByEmployeesId(Integer id);
}
