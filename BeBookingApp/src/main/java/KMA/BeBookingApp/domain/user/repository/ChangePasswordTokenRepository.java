package KMA.BeBookingApp.domain.user.repository;

import KMA.BeBookingApp.domain.user.entity.ChangePasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangePasswordTokenRepository extends JpaRepository<ChangePasswordToken , Long> {

    @Modifying
    @Query("UPDATE ChangePasswordToken c SET c.isAble = false WHERE c.email = :email AND c.isAble = true")
    void disableAllTokensForEmail(@Param("email") String email);


    @Query("SELECT c FROM ChangePasswordToken c WHERE c.jwtId = :jwtId AND c.isAble = true")
    Optional<ChangePasswordToken> findChangePasswordTokenAble(@Param("jwtId") String jwtId);

}
