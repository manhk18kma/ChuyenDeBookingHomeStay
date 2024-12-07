package KMA.BeBookingApp.domain.user.repository;

import KMA.BeBookingApp.domain.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository

public interface RoleRepository extends JpaRepository<Role , Long> {
    Optional<Role> findByRoleName(String roleName);


    @Query("SELECT r FROM Role r " +
            "INNER JOIN UserHasRole uhr on r.id = uhr.role.id WHERE uhr.user.id = :id")
    List<Role> getRolesByUserId(Long id);
}
