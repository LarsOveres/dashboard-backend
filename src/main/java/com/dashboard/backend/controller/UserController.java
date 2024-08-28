//package com.dashboard.backend.controller;
//
//import com.dashboard.backend.dto.UserDto;
//import com.dashboard.backend.model.User;
//import com.dashboard.backend.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Optional;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:5173/")
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // Endpoint voor registratie van een gebruiker
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
//        try {
//            userService.createUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse("User created successfully"));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse(e.getMessage()));
//        }
//    }
//
//    // Endpoint voor het toewijzen van de admin-rol
//    @PostMapping("/{id}/assign-admin")
//    public ResponseEntity<?> assignAdminRole(@PathVariable Long id) {
//        String result = userService.assignAdminRole(id);
//        return ResponseEntity.ok().body(new ApiResponse(result));
//    }
//
//    // Klasse voor JSON respons
//    public static class ApiResponse {
//        private String message;
//
//        public ApiResponse(String message) {
//            this.message = message;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<User> getUserById(@PathVariable Long id) {
////        Optional<User> user = userService.getUserById(id);
////        return user.map(ResponseEntity::ok)
////                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
////    }
//
//}
//
