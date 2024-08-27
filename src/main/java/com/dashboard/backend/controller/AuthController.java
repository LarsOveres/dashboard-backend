package com.dashboard.backend.controller;

import com.dashboard.backend.dto.*;
import com.dashboard.backend.model.User;
import com.dashboard.backend.security.JwtService;
import com.dashboard.backend.service.UserService;
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
@CrossOrigin(origins = "http://localhost:5173/")
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


//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthDto authDto) {
//        UsernamePasswordAuthenticationToken up =
//                new UsernamePasswordAuthenticationToken(authDto.email, authDto.password);
//        try {
//            Authentication auth = authManager.authenticate(up);
//
//            UserDetails ud = (UserDetails) auth.getPrincipal();
//            User user = userService.findByEmail(ud.getUsername());
//            String token = jwtService.generateToken(ud);
//
//            // Maak een AuthResponse object met het token
//            AuthResponse authResponse = new AuthResponse(token);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .body(authResponse);
//        }
//        catch (AuthenticationException ex) {
//            return new ResponseEntity<>(new AuthResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PostMapping("/login")
//    @CrossOrigin(origins = "http://localhost:5173/")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());
        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            String token = jwtService.generateToken(userDetails);

            AuthResponse authResponse = new AuthResponse(token, user.getEmail());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(authResponse);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(new AuthResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
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
    @GetMapping("/users/{email}")
//    @CrossOrigin(origins = "http://localhost:5173/")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> new ResponseEntity<>(
                        new UserDto(user.getEmail()),
                        HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            String response = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }

}
