package KMA.BeBookingApp.domain.user.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidated_token")
public class InvalidatedToken extends AbstractEntity<Long> {
    String jwtId;


    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TokenType tokenType;

}
