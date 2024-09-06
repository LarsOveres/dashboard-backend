package com.dashboard.backend.controller;

import com.dashboard.backend.dto.Mp3FileDto;
import com.dashboard.backend.model.User;
import com.dashboard.backend.service.Mp3FileService;
import com.dashboard.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class Mp3FileController {

    private final Mp3FileService mp3FileService;
    private final UserService userService;

    @Autowired
    public Mp3FileController(Mp3FileService mp3FileService, UserService userService) {
        this.mp3FileService = mp3FileService;
        this.userService = userService;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<Mp3FileDto> uploadFile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("name") String name,
//            @RequestParam("type") String type,
//            @RequestParam("size") Long size,
//            @AuthenticationPrincipal User user) {
//
//        System.out.println("Gebruiker: " + user);
//        System.out.println("Gebruiker ID: " + (user != null ? user.getId() : "Geen gebruiker"));
//
//        try {
//            System.out.println("Ontvangen bestandstype: " + type);
//            System.out.println("Bestandsnaam: " + file.getOriginalFilename());
//            System.out.println("Bestandsformaat " + file.getSize());
//
//            if (user == null || user.getId() == null) {
//                throw new IllegalArgumentException("De gebruiker is niet geldig!");
//            }
//
//            Mp3FileDto savedFile = mp3FileService.saveFile(file, name, type, size, user);
//            return ResponseEntity.ok(savedFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }

    @PostMapping("/upload")
    public ResponseEntity<Mp3FileDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("size") Long size,
            @RequestParam("userId") Long userId) { // Verander user naar userId

        System.out.println("Gebruiker ID: " + userId);

        // Verkrijg de gebruiker op basis van userId
        User user = userService.getUserById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("De gebruiker is niet geldig!");
        }

        try {
            System.out.println("Ontvangen bestandstype: " + type);
            System.out.println("Bestandsnaam: " + file.getOriginalFilename());
            System.out.println("Bestandsformaat " + file.getSize());

            Mp3FileDto savedFile = mp3FileService.saveFile(file, name, type, size, user);
            return ResponseEntity.ok(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
