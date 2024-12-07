package KMA.BeBookingApp.domain.user.entity;
import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity<Long> {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    private Boolean isSetUsername;
    private Boolean isSetPassword;


    @Column(name = "email")
    private String email;

    private  String firstName;

    private  String lastName;

    @Column(name = "fullName")
    private String fullName;

    private String urlAvt;

    private LocalDateTime dateOfBirth;

    private String locale;

    private Integer failedAttempts;

    private String tokenDevice;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    private String phone;

    private String tokenVersion;

}
