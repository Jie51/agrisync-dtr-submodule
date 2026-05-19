package com.example.mauswag.repository;

import com.example.mauswag.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e WHERE e.emp_name = :emp_name")
    Optional<Employee> findByEmp_name(@Param("emp_name") String emp_name);
}
