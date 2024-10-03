package com.dashboard.backend.service;

import com.dashboard.backend.model.Role;
import com.dashboard.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddRole() {

        String roleName = "ROLE_ADMIN";
        Role role = new Role();
        role.setRoleName(roleName);

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.addRole(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldFindRoleByName() {

        String roleName = "ROLE_USER";
        Role role = new Role();
        role.setRoleName(roleName);

        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findByRoleName(roleName);

        assertTrue(result.isPresent());
        assertEquals(roleName, result.get().getRoleName());
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }

    @Test
    void shouldReturnEmptyWhenRoleNotFound() {

        String roleName = "GUEST";

        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findByRoleName(roleName);

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }
}
