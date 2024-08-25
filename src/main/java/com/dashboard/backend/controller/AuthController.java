package com.dashboard.backend.controller;

import com.dashboard.backend.dto.AuthDto;
import com.dashboard.backend.dto.AuthResponse;
import com.dashboard.backend.dto.UserDto;
import com.dashboard.backend.model.User;
import com.dashboard.backend.security.JwtService;
import com.dashboard.backend.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager man, JwtService service, UserService userService) {
        this.authManager = man;
        this.jwtService = service;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken up =
                new UsernamePasswordAuthenticationToken(authDto.email, authDto.password);
        try {
            Authentication auth = authManager.authenticate(up);

            UserDetails ud = (UserDetails) auth.getPrincipal();
            User user = userService.findByEmail(ud.getUsername());
            String token = jwtService.generateToken(ud, user.getId());

            // Maak een AuthResponse object met het token
            AuthResponse authResponse = new AuthResponse(token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(authResponse);
        }
        catch (AuthenticationException ex) {
            return new ResponseEntity<>(new AuthResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
//        try {
//            String response = userService.createUser(userDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }

}
