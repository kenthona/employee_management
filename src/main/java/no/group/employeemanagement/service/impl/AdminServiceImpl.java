package no.group.employeemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.exceptions.ResourceNotFoundException;
import no.group.employeemanagement.constant.Constant;
import no.group.employeemanagement.dto.AdminDto;
import no.group.employeemanagement.dto.ProfileUpdateDto;
import no.group.employeemanagement.model.Admin;
import no.group.employeemanagement.repository.AdminRepository;
import no.group.employeemanagement.service.AdminService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(admin -> modelMapper.map(admin, AdminDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AdminDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ADMIN_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.ADMIN_REQUEST_PROCESS)+ id,null));
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public AdminDto createAdmin(AdminDto adminDto) throws BadRequestException {
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        Admin admin = modelMapper.map(adminDto, Admin.class);
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        return modelMapper.map(adminRepository.save(admin), AdminDto.class);
    }

    @Override
    public AdminDto updateAdmin(Long id, AdminDto adminDto) throws BadRequestException {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ADMIN_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.ADMIN_REQUEST_PROCESS)+ id,null));

        if (!admin.getEmail().equals(adminDto.getEmail()) &&
                adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        modelMapper.map(adminDto, admin);
        if (adminDto.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        }
        return modelMapper.map(adminRepository.save(admin), AdminDto.class);
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ADMIN_REQUEST_PROCESS, Constant.getDataNotFoundMessage(Constant.ADMIN_REQUEST_PROCESS)+ id,null));
        adminRepository.delete(admin);
    }

    @Override
    public AdminDto updateProfile(Admin admin, ProfileUpdateDto profileUpdateDto) throws BadRequestException {
        if (!admin.getEmail().equals(profileUpdateDto.getEmail())) {
            throw new BadRequestException("Cannot change email through profile update");
        }

        modelMapper.map(profileUpdateDto, admin);
        if (profileUpdateDto.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(profileUpdateDto.getPassword()));
        }
        return modelMapper.map(adminRepository.save(admin), AdminDto.class);
    }
}
