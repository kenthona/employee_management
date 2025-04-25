package no.group.employeemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.exceptions.ForbiddenException;
import no.group.employeemanagement.exceptions.ResourceNotFoundException;
import no.group.employeemanagement.dto.AdminDto;
import no.group.employeemanagement.dto.JwtAuthenticationResponseDto;
import no.group.employeemanagement.dto.LoginDto;
import no.group.employeemanagement.model.Admin;
import no.group.employeemanagement.model.Employee;
import no.group.employeemanagement.model.User;
import no.group.employeemanagement.repository.AdminRepository;
import no.group.employeemanagement.repository.EmployeeRepository;
import no.group.employeemanagement.security.JwtTokenProvider;
import no.group.employeemanagement.security.Role;
import no.group.employeemanagement.config.SecurityConfig;
import no.group.employeemanagement.service.AuthService;
import no.group.employeemanagement.utils.TokenUtil;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final SecurityConfig securityConfig;


    @Override
    public JwtAuthenticationResponseDto authenticateUser(LoginDto loginDto) {
        try {
            boolean isAdmin = false;
            boolean isEmployee = false;
            User user = null;

            var admin = adminRepository.findFirstByEmailOrderByCreatedDateDesc(loginDto.getEmail());
            var employee = employeeRepository.findFirstByEmailOrderByCreatedDateDesc(loginDto.getEmail());

            if (admin != null) {
                isAdmin = true;
            }

            if (employee != null) {
                isEmployee = true;
            }

            if (isAdmin && isEmployee) {
                throw new ForbiddenException();
            }

            if (isAdmin) {
                user = admin;
            }

            if (isEmployee) {
                user = employee;
            }

            var jwtResponse = TokenUtil.generate(user.getFirstName(), user.getId());
            var token = jwtResponse.get("token").toString();
            JwtAuthenticationResponseDto response = new JwtAuthenticationResponseDto();
            response.setUserId(admin.getId());
            response.setIsAdmin(isAdmin);
            response.setAccessToken(token);
            return response;

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logoutUser(Admin admin) {

        SecurityContextHolder.clearContext();

        admin.setLastLogoutTime(LocalDateTime.now());
        adminRepository.save(admin);
    }

    @Override
    public Admin getCurrentUser() {
        // 1. Get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Extract username (email in this case)
        String email = authentication.getName();

        // 3. Fetch user from database
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "email", email));
    }

    @Override
    public AdminDto registerAdmin(AdminDto adminDto) throws BadRequestException {
        // 1. Check if email already exists
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        // 2. Map DTO to entity
        Admin admin = modelMapper.map(adminDto, Admin.class);

        // 3. Encrypt password
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));

        // 4. Set default role
        if (adminDto.getRole() == null) {
            admin.setRole(Role.STAFF);
        }

        // 5. Save to database
        Admin savedAdmin = adminRepository.save(admin);

        // 6. Return DTO without password
        return modelMapper.map(savedAdmin, AdminDto.class);
    }
}
