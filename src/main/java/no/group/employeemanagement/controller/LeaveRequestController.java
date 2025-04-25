package no.group.employeemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.exceptions.InvalidLeaveRequestException;
import no.group.employeemanagement.dto.LeaveRequestDto;
import no.group.employeemanagement.service.LeaveRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<List<LeaveRequestDto>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDto>> getLeaveRequestsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByEmployee(employeeId));
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDto> createLeaveRequest(@Valid @RequestBody LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException {
        return new ResponseEntity<>(leaveRequestService.createLeaveRequest(leaveRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequestDto> updateLeaveRequest(@PathVariable Long id, @Valid @RequestBody LeaveRequestDto leaveRequestDto) throws InvalidLeaveRequestException {
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequest(id, leaveRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
