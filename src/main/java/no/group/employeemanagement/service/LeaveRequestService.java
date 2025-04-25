package no.group.employeemanagement.service;

import no.group.employeemanagement.exceptions.InvalidLeaveRequestException;
import no.group.employeemanagement.dto.LeaveRequestDto;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveRequestDto> getAllLeaveRequests();
    List<LeaveRequestDto> getLeaveRequestsByEmployee(Long employeeId);
    LeaveRequestDto createLeaveRequest(LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException;
    LeaveRequestDto updateLeaveRequest(Long id, LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException;
    void deleteLeaveRequest(Long id);
}