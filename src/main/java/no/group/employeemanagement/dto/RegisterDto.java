package no.group.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDto {

    private Long id;

    @NotBlank(message = "First name wajib diisi.")
    private String firstName;

    @NotBlank(message = "Last name wajib diisi.")
    private String lastName;

    @NotBlank(message = "Email wajib diisi.")
    @Email(message = "Format email tidak valid.")
    private String email;

    @NotNull(message = "Tanggal lahir wajib diisi.")
    private LocalDate birthDate;

    @NotBlank(message = "Jenis kelamin wajib diisi.")
    private String gender;

    @NotBlank(message = "Password wajib diisi.")
    private String password;
}

