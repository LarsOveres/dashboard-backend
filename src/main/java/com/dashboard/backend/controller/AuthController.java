package com.dashboard.backend.controller;

import com.dashboard.backend.dto.*;
import com.dashboard.backend.security.JwtService;
import com.dashboard.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager man, JwtService service, UserService userService) {
        this.authManager = man;
        this.jwtService = service;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());
        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            AuthResponse authResponse = new AuthResponse(token, userDetails.getUsername());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(authResponse);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(new AuthResponse("Bad credentials"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        System.out.println("Opgehaald email: " + email);  // Log het emailadres
        return userService.getUserByEmail(email)
                .map(user -> new ResponseEntity<>(
                        new UserDto(user.getId(), user.getEmail(), user.getArtistName()),
                        HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        logger.info("Request to delete user with id: {}", id);
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
