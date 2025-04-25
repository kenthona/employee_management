package no.group.employeemanagement.repository;

import no.group.employeemanagement.model.Admin;
import no.group.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Employee findFirstByEmailOrderByCreatedDateDesc(String email);

}
