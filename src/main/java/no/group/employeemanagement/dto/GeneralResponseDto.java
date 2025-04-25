package no.group.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponseDto<T> extends StatusResponseDto implements Serializable {

    private static final long serialVersionUID = 5429074432850885171L;

    @JsonProperty(index = 12)
    private T result;

}
