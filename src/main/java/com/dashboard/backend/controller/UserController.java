package com.dashboard.backend.controller;

import com.dashboard.backend.dto.UserDto;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            String response = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/allusers")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.email = user.getEmail();
                    userDto.fName = user.getfName();
                    userDto.lName = user.getlName();
                    userDto.password = user.getPassword();
                    userDto.roleName = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
                    return userDto;

                })
                .collect(Collectors.toList());
    }


}

