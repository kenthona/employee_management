package no.group.employeemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.exceptions.InvalidLeaveRequestException;
import no.group.employeemanagement.exceptions.ResourceNotFoundException;
import no.group.employeemanagement.constant.Constant;
import no.group.employeemanagement.dto.LeaveRequestDto;
import no.group.employeemanagement.model.Employee;
import no.group.employeemanagement.model.LeaveRequest;
import no.group.employeemanagement.repository.EmployeeRepository;
import no.group.employeemanagement.repository.LeaveRequestRepository;
import no.group.employeemanagement.service.LeaveRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {


    private final LeaveRequestRepository leaveRequestRepository;

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

        @Override
        public List<LeaveRequestDto> getAllLeaveRequests() {
            return leaveRequestRepository.findAll().stream()
                    .map(leave -> modelMapper.map(leave, LeaveRequestDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public List<LeaveRequestDto> getLeaveRequestsByEmployee(Long employeeId) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException(Constant.LEAVE_REQUEST_PROCESS, Constant.DATA_NOT_FOUND, Constant.getDataNotFoundMessage(Constant.LEAVE_REQUEST_PROCESS)+employeeId));

            return leaveRequestRepository.findByEmployee(employee).stream()
                    .map(leave -> modelMapper.map(leave, LeaveRequestDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public LeaveRequestDto createLeaveRequest(LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException {
            Employee employee = employeeRepository.findById(leaveRequestDto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(Constant.LEAVE_REQUEST_PROCESS, Constant.DATA_NOT_FOUND, Constant.getDataNotFoundMessage(Constant.LEAVE_REQUEST_PROCESS)+leaveRequestDto.getEmployeeId()));

            validateLeaveRequest(employee, leaveRequestDto);

            LeaveRequest leaveRequest = modelMapper.map(leaveRequestDto, LeaveRequest.class);
            leaveRequest.setEmployee(employee);
            return modelMapper.map(leaveRequestRepository.save(leaveRequest), LeaveRequestDto.class);
        }

        @Override
        public LeaveRequestDto updateLeaveRequest(Long id, LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException {
            LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(Constant.LEAVE_REQUEST_PROCESS, Constant.DATA_NOT_FOUND, Constant.getDataNotFoundMessage(Constant.LEAVE_REQUEST_PROCESS)+id));

            Employee employee = employeeRepository.findById(leaveRequestDto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(Constant.LEAVE_REQUEST_PROCESS, Constant.DATA_NOT_FOUND, Constant.getDataNotFoundMessage(Constant.LEAVE_REQUEST_PROCESS)+leaveRequestDto.getEmployeeId()));

            validateLeaveRequest(employee, leaveRequestDto);

            modelMapper.map(leaveRequestDto, leaveRequest);
            leaveRequest.setEmployee(employee);
            return modelMapper.map(leaveRequestRepository.save(leaveRequest), LeaveRequestDto.class);
        }

        @Override
        public void deleteLeaveRequest(Long id) {
            LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(Constant.LEAVE_REQUEST_PROCESS, Constant.DATA_NOT_FOUND, Constant.getDataNotFoundMessage(Constant.LEAVE_REQUEST_PROCESS)+id));
            leaveRequestRepository.delete(leaveRequest);
        }



    private LeaveRequestDto mapToDto(LeaveRequest leaveRequest) {
        return modelMapper.map(leaveRequest, LeaveRequestDto.class);
    }

    private LeaveRequest mapToEntity(LeaveRequestDto leaveRequestDto) {
        return modelMapper.map(leaveRequestDto, LeaveRequest.class);
    }

    private void validateLeaveRequest(Employee employee, LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException {
        // Check if start date is before end date
        if (leaveRequestDto.getStartDate().isAfter(leaveRequestDto.getEndDate())) {
            throw new InvalidLeaveRequestException("Start date must be before end date");
        }

        // Calculate total days
        long daysRequested = ChronoUnit.DAYS.between(leaveRequestDto.getStartDate(), leaveRequestDto.getEndDate()) + 1;

        // Check if more than 1 day in same month
        if (leaveRequestDto.getStartDate().getMonth() == leaveRequestDto.getEndDate().getMonth() && daysRequested > 1) {
            throw new InvalidLeaveRequestException("Only 1 day leave is allowed per month");
        }

        // Check total days in year (12 days max)
        int currentYear = LocalDate.now().getYear();
        List<LeaveRequest> yearlyLeaves = leaveRequestRepository.findByEmployeeAndYear(employee.getId(), currentYear);

        long totalDaysUsed = yearlyLeaves.stream()
                .mapToLong(leave -> ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1)
                .sum();

        if (totalDaysUsed + daysRequested > 12) {
            throw new InvalidLeaveRequestException("Maximum 12 days of leave allowed per year");
        }
    }
}

