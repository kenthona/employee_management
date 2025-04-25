package no.group.employeemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.group.employeemanagement.dto.AdminDto;
import no.group.employeemanagement.dto.ProfileUpdateDto;
import no.group.employeemanagement.model.Admin;
import no.group.employeemanagement.service.AdminService;
import no.group.employeemanagement.service.AuthService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> createAdmin(@Valid @RequestBody AdminDto adminDto) throws BadRequestException {
        return new ResponseEntity<>(adminService.createAdmin(adminDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminDto adminDto) throws BadRequestException {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<AdminDto> updateProfile(
            @CurrentUser Admin currentAdmin,
            @Valid @RequestBody ProfileUpdateDto profileUpdateDto) throws BadRequestException {
        return ResponseEntity.ok(adminService.updateProfile(currentAdmin, profileUpdateDto));
    }
}