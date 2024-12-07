package KMA.BeBookingApp.app.exception;

import KMA.BeBookingApp.domain.common.enumType.ErrorType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private ErrorType type;

}
