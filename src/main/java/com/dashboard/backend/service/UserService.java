package com.dashboard.backend.service;

import com.dashboard.backend.dto.UserDto;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.RoleRepository;
import com.dashboard.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<User> getAllUsers() {
        return userRepos.findAll();
    }

    public String createUser(UserDto userDto) {

        if (userRepos.findByEmail(userDto.email).isPresent()) {
            return "User already exists";
        }

        User newUser = new User();
        newUser.setEmail(userDto.email);
        newUser.setfName(userDto.fName);
        newUser.setlName(userDto.lName);
        newUser.setPassword(encoder.encode(userDto.password));

        Set<Role> roles = userDto.roleName.stream()
                .map(roleName -> roleRepos.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        newUser.setRoles(roles);
        userRepos.save(newUser);
        return "User created successfully";
    }
}
