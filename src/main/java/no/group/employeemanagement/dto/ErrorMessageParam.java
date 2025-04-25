package no.group.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1885724610172047758L;

    private String otpAttemptLeft;
    private String noReference;
    private String field;
    private String minAmountPerTransactionLimit;
    private String responseCode;
    private String ttlForgotPassword;
    private String idpel;
    private String minBalance;
}
