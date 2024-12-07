package KMA.BeBookingApp.domain.user.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "change_password_token")
public class ChangePasswordToken extends AbstractEntity<Long> {
    @Column
    private String jwtId;

    @Column
    private String email;

    @Column
    private Boolean isAble = true;


}
