package com.dashboard.backend.component;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Controleer of de rol 'USER' al bestaat
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            roleRepository.save(new Role("USER"));
        }

        // Controleer of de rol 'ADMIN' al bestaat
        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
            roleRepository.save(new Role("ADMIN"));
        }
    }
}
