package KMA.BeBookingApp.domain.user.repository;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import KMA.BeBookingApp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> validateAndGetUserByAuthenticationName(@Param("email") String email);


    @Query("""
    SELECT u.failedAttempts 
    FROM User u 
    WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail
""")
    Optional<Integer> getFailedAttempts(String usernameOrEmail);


}
