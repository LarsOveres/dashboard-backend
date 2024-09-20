package com.dashboard.backend.service;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.RoleRepository;
import com.dashboard.backend.repository.UserRepository;
import com.dashboard.backend.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepos;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepos;

    @Autowired
    public UserService(UserRepository userRepos, RoleService roleService, PasswordEncoder encoder, RoleRepository roleRepos) {
        this.userRepos = userRepos;
        this.roleService = roleService;
        this.encoder = encoder;
        this.roleRepos = roleRepos;
    }

    public boolean isEmailTaken(String email) {
        return userRepos.findByEmail(email).isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepos.findByEmail(email); // Zorg dat findByEmail bestaat in je repository
    }

    public User createUser(String email, String password, String artistName) {
        if (userRepos.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail is al in gebruik.");
        }

        // Haal de rol 'USER' op
        Role userRole = roleService.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER does not exist"));

        // Maak de nieuwe gebruiker aan
        User user = new User();
        user.setPassword(encoder.encode(password)); // Encode het wachtwoord
        user.setArtistName(artistName);
        user.setEmail(email);
        user.setRole(userRole);

        // Sla de gebruiker op in de database
        return userRepos.save(user);
    }

    // Voeg een nieuwe methode toe om de rol van een gebruiker bij te werken
    public User updateUserRole(Long userId, String newRoleName) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = roleRepos.findByRoleName(newRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        return userRepos.save(user);
    }


    @Transactional
    public String assignAdminRole(Long userId) {
        // Zoek de gebruiker
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Zoek de rol "ADMIN"
        Role adminRole = roleService.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role 'ADMIN' not found"));

        // Stel de rol in voor de gebruiker
        user.setRole(adminRole);
        userRepos.save(user);

        return "Admin role assigned successfully";
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (userRepos.existsById(id)) {
            userRepos.deleteById(id);
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepos.findByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden"));
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


