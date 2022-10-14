package com.leverx.internship.project.project.repository.entity;

import com.leverx.internship.project.user.repository.entity.User;
import java.time.Instant;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Project {

  @Id
  @SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
  private int id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "date_start", nullable = false)
  private Instant dateStart;

  @Column(name = "date_end", nullable = false)
  private Instant dateEnd;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private Instant updatedAt;

  @Column(name = "created_by", nullable = false)
  @CreatedBy
  private String createdBy;

  @Column(name = "updated_by", nullable = false)
  @LastModifiedBy
  private String updatedBy;

  @ManyToMany
  @JoinTable(
      name = "employee_has_projects",
      joinColumns = @JoinColumn(name = "project_id"),
      inverseJoinColumns = @JoinColumn(name = "employee_id"))
  private Set<User> employees;
}