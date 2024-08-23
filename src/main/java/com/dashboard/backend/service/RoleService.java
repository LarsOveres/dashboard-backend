package com.dashboard.backend.service;


import com.dashboard.backend.model.Role;
import com.dashboard.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role addRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

}