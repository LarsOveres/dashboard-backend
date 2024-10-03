package com.dashboard.backend.controller;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.service.RoleService;
import com.dashboard.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Map<String, String> roleData) {
        String roleName = roleData.get("roleName");

        try {
            Role newRole = roleService.addRole(roleName);
            return new ResponseEntity<>(newRole, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/user/{userId}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long userId,
                                               @RequestParam String newRoleName) {
        try {
            User updatedUser = userService.updateUserRole(userId, newRoleName);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/role")
    public ResponseEntity<Map<String, String>> getUserRole(Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        Map<String, String> response = new HashMap<>();
        response.put("roleName", user.getRole().getRoleName());

        return ResponseEntity.ok(response);
    }

}
