package com.dashboard.backend.service;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.RoleRepository;
import com.dashboard.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

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

    public Optional<User> getUserByEmail(String email) {
        return userRepos.findByEmail(email);
    }

    public User createUser(String email, String password, String artistName) {
        if (userRepos.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail is al in gebruik.");
        }

        Role userRole = roleService.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER does not exist"));

        User user = new User();
        user.setPassword(encoder.encode(password));
        user.setArtistName(artistName);
        user.setEmail(email);
        user.setRole(userRole);

        return userRepos.save(user);
    }

    public User updateUserRole(Long userId, String newRoleName) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = roleRepos.findByRoleName(newRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        return userRepos.save(user);
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
}


