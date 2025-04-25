package no.group.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponseDto implements Serializable {

    private static final long serialVersionUID = -7208658160002406393L;

    @JsonProperty(index = 1)
    private String code;

    @JsonProperty(index = 2)
    private String codeSystem;

    @JsonProperty(index = 3)
    private Integer waitingTimeMin;

    @JsonProperty(index = 4)
    private String message;

    @JsonProperty(index = 5)
    private String engMessage;

    @JsonProperty(index = 6)
    private String messageError;

    @JsonProperty(index = 7)
    private String remainingAttempt;

    @JsonProperty(index = 8)
    private String activityRefCode;

    @JsonIgnore
    public boolean isSuccess(){
        return "00".equals(this.code);
    }

    @JsonIgnore
    public boolean isSuspect(){
        return "68".equals(this.code);
    }

}
