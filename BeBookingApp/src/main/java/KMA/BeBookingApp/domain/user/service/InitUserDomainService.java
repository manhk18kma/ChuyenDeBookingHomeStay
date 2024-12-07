package KMA.BeBookingApp.domain.user.service;

import KMA.BeBookingApp.domain.user.entity.Role;
import KMA.BeBookingApp.domain.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class InitUserDomainService implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound("USER", "Standard user with limited permissions");
        createRoleIfNotFound("ADMIN", "Administrator with full permissions");
    }

    private void createRoleIfNotFound(String roleName, String description) {
        if (roleRepository.findByRoleName(roleName).isEmpty()) {
            Role role = new Role();
            role.setRoleName(roleName);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}
