package com.dashboard.backend.controller;

import com.dashboard.backend.dto.*;
import com.dashboard.backend.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager man, JwtService service, UserService userService, PasswordEncoder passwordEncoder) {
        this.authManager = man;
        this.jwtService = service;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());
        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            User user = userService.findByEmail(userDetails.getUsername());
            String token = jwtService.generateToken(userDetails);

            AuthResponse authResponse = new AuthResponse(token, userDetails.getUsername());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(authResponse);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(new AuthResponse("Bad credentials"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setArtistName(userDto.getArtistName());
            user.setPassword(userDto.getPassword());
//            if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
//                throw new IllegalArgumentException("Password cannot be null or empty");
//            }

//            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
//            user.setPassword(encodedPassword);

            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> new ResponseEntity<>(
                        new UserDto(user.getEmail()),
                        HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping("/{id}/assign-admin")
    public ResponseEntity<?> assignAdminRole(@PathVariable Long id) {
        String result = userService.assignAdminRole(id);
        return ResponseEntity.ok().body(result);
    }


//    @GetMapping("/users/{id}")
//    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
//        Optional<User> user = userService.getUserById(id);
//        if (user.isPresent()) {
//            UserDto userDto = new UserDto(user.get().getId(), user.get().getEmail(), user.get().getArtistName());
//            return ResponseEntity.ok(userDto);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

//    @GetMapping("/users/{id}")
//    public UserDto getUserById(@PathVariable Long id) {
//        return userService.getUserById(id)
//                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getArtistName()))
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//    }

    //    @GetMapping("/users/{id}")
//    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
//        return userService.getUserById(id)
//                .map(user -> new ResponseEntity<>(
//                        new UserDto(user.getId(), user.getEmail(), user.getArtistName()),
//                        HttpStatus.OK))
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//    }


//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
//        try {
//            String response = userService.createUser(userDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

//    @DeleteMapping("/users/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUserById(id);
//        return "User deleted";
//    }

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
