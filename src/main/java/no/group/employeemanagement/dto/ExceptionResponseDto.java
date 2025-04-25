package no.group.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDto {
    String codeSystem;
    String code;
    String errorMessage;
    String message;
}

