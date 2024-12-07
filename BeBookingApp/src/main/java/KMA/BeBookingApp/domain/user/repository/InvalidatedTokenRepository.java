package KMA.BeBookingApp.domain.user.repository;

import KMA.BeBookingApp.domain.user.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken , Long> {
    boolean existsByJwtId(String jwtId);
}
