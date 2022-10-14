
INSERT INTO users (id, password, email, first_name, last_name, is_active, role_, created_at, updated_at, department_id)
VALUES (2, 'password', 'mail1@mail.com', 'User23', 'User1' , true, 'ADMIN', current_date, current_date, 2),
       (3, 'password', 'mail2@mail.com', 'User23', 'User2' , true, 'EMPLOYEE', current_date, current_date, 3),
       (4, 'password', 'mail3@mail.com', 'User3', 'User3' , true, 'LEAD', current_date, current_date, 4);

INSERT INTO projects(id, name, description, date_start, date_end, created_at, created_by, updated_at, updated_by)
VALUES (2, 'name2', 'description', '2022-02-01', '2022-02-01', '2022-02-01', 2, '2022-02-01', 2),
       (3, 'name3', 'description', '2022-02-01', '2022-02-01', '2022-02-01', 2, '2022-02-01', 2),
       (4, 'name4', 'description', '2022-02-01', '2022-02-01', '2022-02-01', 2, '2022-02-01', 2);

INSERT INTO employee_has_projects(employee_id, project_id)
VALUES (3, 4);

INSERT INTO departments(id, name, description, created_at, created_by, updated_at, updated_by)
VALUES (2, 'name2', 'description', '2022-02-01', 0, '2022-02-01', 0),
       (3, 'name3', 'description', '2022-02-01', 0, '2022-02-01', 0),
       (4, 'name4', 'description', '2022-02-01', 0, '2022-02-01', 0);

INSERT INTO department_has_projects(department_id, project_id)
VALUES (3, 4);


