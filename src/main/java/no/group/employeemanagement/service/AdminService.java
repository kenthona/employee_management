package no.group.employeemanagement.service;

import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.dto.AdminDto;
import no.group.employeemanagement.dto.ProfileUpdateDto;
import no.group.employeemanagement.dto.RegisterDto;
import no.group.employeemanagement.model.Admin;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<AdminDto> getAllAdmins();

    AdminDto getAdminById(Long id);


    RegisterDto createAdmin(RegisterDto adminDto) throws BadRequestException;

    AdminDto updateAdmin(Long id, AdminDto adminDto) throws BadRequestException;

    void deleteAdmin(Long id);

    AdminDto updateProfile(Admin admin, ProfileUpdateDto profileUpdateDto) throws BadRequestException;
}
