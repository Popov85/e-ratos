package ua.edu.ratos.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ua.edu.ratos.dao.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}