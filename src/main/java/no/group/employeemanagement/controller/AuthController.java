package no.group.employeemanagement.controller;

import jakarta.validation.Valid;
import no.group.employeemanagement.dto.JwtAuthenticationResponseDto;
import no.group.employeemanagement.dto.LoginDto;
import no.group.employeemanagement.model.Admin;
import no.group.employeemanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) throws Exception {
        return ResponseEntity.ok(authService.authenticateUser(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@CurrentUser Admin admin) {
        authService.logoutUser(admin);
        return ResponseEntity.noContent().build();
    }
}
