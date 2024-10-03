package com.dashboard.backend.component;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.RoleRepository;
import com.dashboard.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
//public class DataLoader implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//
//    public DataLoader(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Voeg de USER rol toe als deze nog niet bestaat
//        if (roleRepository.findByRoleName("USER").isEmpty()) {
//            roleRepository.save(new Role("USER"));
//        }
//
//        // Voeg de ADMIN rol toe als deze nog niet bestaat
//        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
//            roleRepository.save(new Role("ADMIN"));
//        }
//    }
//}

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByRoleName("ROLE_USER").orElse(null);
        if (userRole == null) {
            userRole = roleRepository.save(new Role("ROLE_USER"));
        }

        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN").orElse(null);
        if (adminRole == null) {
            adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
        }

        // Voeg een admin testgebruiker toe als deze nog niet bestaat
        if (userService.getUserByEmail("admin@test.com").isEmpty()) {
            userService.createUser("admin@test.com", "admin123", "Admin");
        }

        // Voeg een normale testgebruiker toe als deze nog niet bestaat
        if (userService.getUserByEmail("user@test.com").isEmpty()) {
            userService.createUser("user@test.com", "user123", "User");
        }
    }
}

