package no.group.employeemanagement.exceptions;

import no.group.employeemanagement.dto.ExceptionResponseDto;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String sourceSystem, String code, String message) {
        exceptionBuilder(sourceSystem, code, null, message);
    }

    private void exceptionBuilder(String sourceSystem, String code, String messageError, String message) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setCodeSystem(sourceSystem);
        exceptionResponseDto.setCode(code);
        exceptionResponseDto.setMessage(message);
        exceptionResponseDto.setErrorMessage(messageError);
    }
}
