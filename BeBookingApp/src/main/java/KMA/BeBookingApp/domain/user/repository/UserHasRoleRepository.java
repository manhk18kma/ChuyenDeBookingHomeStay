package KMA.BeBookingApp.domain.user.repository;

import KMA.BeBookingApp.domain.user.entity.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserHasRoleRepository extends JpaRepository<UserHasRole , Long> {
}
