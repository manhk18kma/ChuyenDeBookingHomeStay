package KMA.BeBookingApp.domain.user.entity;
import KMA.BeBookingApp.domain.common.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends AbstractEntity<Long> {
    @Column(name = "role_name",  length = 100)
    private String roleName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
