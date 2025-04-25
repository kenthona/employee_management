package no.group.employeemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.exceptions.ResourceNotFoundException;
import no.group.employeemanagement.constant.Constant;
import no.group.employeemanagement.dto.EmployeeDto;
import no.group.employeemanagement.dto.EmployeeWithLeavesDto;
import no.group.employeemanagement.dto.LeaveRequestDto;
import no.group.employeemanagement.model.Employee;
import no.group.employeemanagement.repository.EmployeeRepository;
import no.group.employeemanagement.service.EmployeeService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeWithLeavesDto> getAllEmployeesWithLeaves() {
        return employeeRepository.findAll().stream()
                .map(employee -> {
                    EmployeeWithLeavesDto dto = modelMapper.map(employee, EmployeeWithLeavesDto.class);
                    dto.setLeaveRequests(employee.getLeaveRequests().stream()
                            .map(leave -> modelMapper.map(leave, LeaveRequestDto.class))
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.EMPLOYEE_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.EMPLOYEE_REQUEST_PROCESS)+ id,null));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) throws BadRequestException {
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        return modelMapper.map(employeeRepository.save(employee), EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) throws BadRequestException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.EMPLOYEE_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.EMPLOYEE_REQUEST_PROCESS)+id, null));

        if (!employee.getEmail().equals(employeeDto.getEmail()) &&
                employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        modelMapper.map(employeeDto, employee);
        return modelMapper.map(employeeRepository.save(employee), EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.EMPLOYEE_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.EMPLOYEE_REQUEST_PROCESS)+id,null));
        employeeRepository.delete(employee);
    }
}
