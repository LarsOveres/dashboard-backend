package com.dashboard.backend.controller;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

//    @Autowired
//    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;

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
}
