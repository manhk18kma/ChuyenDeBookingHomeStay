package KMA.BeBookingApp.app.exception;

import KMA.BeBookingApp.domain.common.enumType.ErrorType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ErrorResponseMultipleField {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String , String> message;
    private String path;
    private ErrorType type;
}
