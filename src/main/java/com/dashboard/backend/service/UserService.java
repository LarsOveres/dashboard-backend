package com.dashboard.backend.service;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.RoleRepository;
import com.dashboard.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepos;
    private final RoleRepository roleRepos;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepos, RoleRepository roleRepos, PasswordEncoder encoder) {
        this.userRepos = userRepos;
        this.roleRepos = roleRepos;
        this.encoder = encoder;
    }

    public String assignAdminRole(Long userId) {
        Optional<User> userOpt = userRepos.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        Optional<Role> adminRoleOpt = Optional.ofNullable(roleRepos.findByRoleName("ADMIN"));
        if (!adminRoleOpt.isPresent()) {
            throw new RuntimeException("Role 'ADMIN' not found");
        }
        Role adminRole = adminRoleOpt.get();

        user.getRoles().add(adminRole);
        userRepos.save(user);
        return "Admin role assigned successfully";
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (userRepos.existsById((id))) {
            userRepos.deleteById((id));
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = userRepos.findByEmail(email);
        System.out.println("Gevonden gebruiker: " + user);
        return userRepos.findByEmail(email);
    }

    @Transactional
    public void createUser(User user) {
        if (userRepos.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        Role userRole = roleRepos.findByRoleName("USER");
        if (userRole == null) {
            throw new RuntimeException("Default role 'USER' not found");
        }

        user.getRoles().add(userRole);

        userRepos.save(user);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public Optional<User> getUserById(Long userId) {
        return userRepos.findById(userId);
    }
}
