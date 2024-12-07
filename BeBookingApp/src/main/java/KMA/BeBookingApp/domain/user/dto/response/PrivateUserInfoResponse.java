package KMA.BeBookingApp.domain.user.dto.response;

import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrivateUserInfoResponse {
    private String username;
    private Boolean isSetUsername;
    private String email;
    private  String firstName;
    private  String lastName;
    private String fullName;
    private String urlAvt;
    private LocalDateTime dateOfBirth;
    private String locale;
    Gender gender;
    private String phone;

    public PrivateUserInfoResponse(String username, Boolean isSetUsername, String email, String firstName, String lastName, String fullName, String urlAvt, LocalDateTime dateOfBirth, String locale, Gender gender, String phone) {
        this.username = username;
        this.isSetUsername = isSetUsername;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.urlAvt = urlAvt;
        this.dateOfBirth = dateOfBirth;
        this.locale = locale;
        this.gender = gender;
        this.phone = phone;
    }

    public PrivateUserInfoResponse() {
    }
}
