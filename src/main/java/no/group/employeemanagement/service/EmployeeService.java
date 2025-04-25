package no.group.employeemanagement.service;

import no.group.employeemanagement.dto.EmployeeDto;
import no.group.employeemanagement.dto.EmployeeWithLeavesDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();
    List<EmployeeWithLeavesDto> getAllEmployeesWithLeaves();
    EmployeeDto getEmployeeById(Long id);
    EmployeeDto createEmployee(EmployeeDto employeeDto) throws BadRequestException;
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) throws BadRequestException;
    void deleteEmployee(Long id);
}
