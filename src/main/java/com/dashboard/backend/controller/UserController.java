package com.dashboard.backend.controller;

import com.dashboard.backend.dto.RoleDto;
import com.dashboard.backend.dto.UserDto;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.service.RoleService;
import com.dashboard.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

        private final UserService userService;

        @Autowired
        public UserController(UserService userService) {
            this.userService = userService;
        }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto.getEmail(), userDto.getPassword(), userDto.getArtistName());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long userId,
                                               @RequestBody Map<String, String> roleData) {
        String newRoleName = roleData.get("roleName");

        try {
            userService.updateUserRole(userId, newRoleName);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteRole(@RequestBody RoleDto roleDto) {
//        try {
//            RoleService.deleteRole(roleDto);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }



//    @PutMapping("/{userId}/role")
//    public ResponseEntity<Void> updateUserRole(@PathVariable Long userId,
//                                               @RequestBody Map<String, String> roleData) {
//        String newRoleName = roleData.get("roleName");
//
//        try {
//            userService.updateUserRole(userId, newRoleName);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}