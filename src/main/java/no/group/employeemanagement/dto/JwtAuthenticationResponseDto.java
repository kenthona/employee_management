package no.group.employeemanagement.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtAuthenticationResponseDto {
    private String accessToken;
    private Long userId;
    private Boolean isAdmin;
    public JwtAuthenticationResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
