package KMA.BeBookingApp.app.config.security;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import KMA.BeBookingApp.app.exception.ErrorResponse;
import KMA.BeBookingApp.domain.common.enumType.ErrorType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(
//            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException, ServletException {
//
//        response.setStatus(errorCode.getStatusCode().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        ApiResponse<?> apiResponse = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
//        response.flushBuffer();
//    }
//}


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        AppErrorCode errorCode = AppErrorCode.UNAUTHENTICATED;

        // Tạo ErrorResponse và thiết lập các giá trị
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(errorCode.getStatusCode().value());
        errorResponse.setMessage("UNAUTHENTICATED");
        errorResponse.setType(ErrorType.SINGLE);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError("UNAUTHENTICATED");


        // Chuyển đổi ErrorResponse thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        // Thiết lập response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonErrorResponse); // Ghi JSON vào response
    }
}
