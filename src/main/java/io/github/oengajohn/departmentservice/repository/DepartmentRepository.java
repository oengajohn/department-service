package io.github.oengajohn.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.oengajohn.departmentservice.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    
}
