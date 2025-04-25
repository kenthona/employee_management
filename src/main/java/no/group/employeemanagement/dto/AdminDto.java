package no.group.employeemanagement.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.security.Role;

@Data
@RequiredArgsConstructor
public class AdminDto {
    String email;
    String password;
    Role role;
}
