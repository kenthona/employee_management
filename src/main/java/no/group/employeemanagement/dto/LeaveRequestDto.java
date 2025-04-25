package no.group.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LeaveRequestDto {
    Long employeeId;
    String reason;
    LocalDate startDate;
    LocalDate endDate;
}
