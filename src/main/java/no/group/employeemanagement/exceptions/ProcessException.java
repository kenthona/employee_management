package no.group.employeemanagement.exceptions;


import lombok.Getter;
import no.group.employeemanagement.dto.ErrorMessageParam;
import no.group.employeemanagement.dto.GeneralResponseDto;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class ProcessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8951512887557038928L;

    private GeneralResponseDto generalResponseDto;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private ErrorMessageParam errorMessageParam;

    public ProcessException(String sourceSystem, String errorCode) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, String message) {
        super(message);
        responseDtoBuilder(sourceSystem, errorCode, message, null, null, null, null, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, HttpStatus httpStatus) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, httpStatus, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        responseDtoBuilder(sourceSystem, errorCode, message, null, null, null, null, httpStatus, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, String idnMessage, String engMessage) {
        responseDtoBuilder(sourceSystem, errorCode, null, idnMessage, engMessage, null, null, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, Integer waitingTimeMin, String idnMessage, String engMessage) {
        responseDtoBuilder(sourceSystem, errorCode, null, idnMessage, engMessage, waitingTimeMin, null, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, String remainingAttempt, String idnMessage, String engMessage) {
        responseDtoBuilder(sourceSystem, errorCode, null, idnMessage, engMessage, null, remainingAttempt, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, String idnMessage, String engMessage, HttpStatus httpStatus) {
        responseDtoBuilder(sourceSystem, errorCode, null, idnMessage, engMessage, null, null, httpStatus, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, Integer waitingTimeMin) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, waitingTimeMin, null, null, null, null);
    }

    public ProcessException(String sourceSystem, String errorCode, ErrorMessageParam param) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, null, param, null);
    }

    public <T> ProcessException(String sourceSystem, String errorCode, ErrorMessageParam param, T result) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, null, param, result);
    }

    public <T> ProcessException(String sourceSystem, String errorCode, T result) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, null, null, result);
    }

    public <T> ProcessException(String sourceSystem, String errorCode, T result, HttpStatus httpStatus) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, null, httpStatus, null, result);
    }

    public <T> ProcessException(String sourceSystem, String errorCode, T result, String message) {
        responseDtoBuilder(sourceSystem, errorCode, null, message, null, null, null, httpStatus, null, result);
    }

    public <T> ProcessException(String sourceSystem, String errorCode, String remainingAttempt, ErrorMessageParam param) {
        responseDtoBuilder(sourceSystem, errorCode, null, null, null, null, remainingAttempt, null, param, null);
    }

    @SuppressWarnings("all")
    private <T> void responseDtoBuilder(String sourceSystem, String code, String messageError, String idMessage, String enMessage,
                                        Integer waitingTimeMin, String remainingAttempt, HttpStatus httpStatus, ErrorMessageParam param, T result) {
        generalResponseDto = new GeneralResponseDto();
        generalResponseDto.setCodeSystem(sourceSystem);
        generalResponseDto.setCode(code);
        generalResponseDto.setMessageError(messageError);
        generalResponseDto.setMessage(idMessage);
        generalResponseDto.setEngMessage(enMessage);
        generalResponseDto.setWaitingTimeMin(waitingTimeMin);
        generalResponseDto.setRemainingAttempt(remainingAttempt);

        if (result != null) {
            generalResponseDto.setResult(result);
        }

        if (httpStatus != null) {
            this.httpStatus = httpStatus;
        }
        errorMessageParam = param;
    }
}