package no.group.employeemanagement.service;

import no.group.employeemanagement.dto.AdminDto;
import no.group.employeemanagement.dto.JwtAuthenticationResponseDto;
import no.group.employeemanagement.dto.LoginDto;
import no.group.employeemanagement.model.Admin;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    JwtAuthenticationResponseDto authenticateUser(LoginDto loginDto) throws Exception;

    void logoutUser(Admin admin);

    Admin getCurrentUser();

    AdminDto registerAdmin(AdminDto adminDto) throws BadRequestException;
}
