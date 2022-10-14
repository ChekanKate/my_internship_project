package com.leverx.internship.project.project.repository;

import com.leverx.internship.project.project.repository.entity.Project;
import java.time.Instant;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository
    extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

  @Query(
      value =
          "select p from Project p "
              + "left join p.employees employees "
              + "where employees.id = ?1 "
              + "and ((p.dateStart between ?2 and ?3 and p.dateEnd between ?2 and ?3) "
              + "or (p.dateStart < ?2 and p.dateEnd > ?3) "
              + "or (p.dateStart > ?2 and p.dateStart < ?3 and p.dateEnd > ?3) "
              + "or (p.dateStart < ?2 and p.dateEnd < ?3 and p.dateEnd > ?2))")
  Collection<Project> findActiveProjectForCurrentMonthByEmployeeId(
      Integer id, Instant dateStart, Instant dateEnd);

  @Query("select p from Project p "
      + "left join p.employees employees "
      + "where employees.id = ?1 and p.dateEnd > ?2")
  Collection<Project> findProjectsByEmployeesIdAndDateEndAfter(
      Integer idEmployee, Instant date);
}