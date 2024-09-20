package com.dashboard.backend.controller;

import com.dashboard.backend.dto.Mp3FileDto;

import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.User;

import com.dashboard.backend.repository.Mp3FileRepository;
import com.dashboard.backend.service.Mp3FileService;
import com.dashboard.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/files")
public class Mp3FileController {

    @Autowired
    private final Mp3FileService mp3FileService;
    private final UserService userService;
    private final Mp3FileRepository mp3FileRepository;

    @Autowired
    public Mp3FileController(Mp3FileService mp3FileService, UserService userService, Mp3FileRepository mp3FileRepository) {
        this.mp3FileService = mp3FileService;
        this.userService = userService;
        this.mp3FileRepository = mp3FileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Mp3FileDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("size") Long size,
            Principal principal) {

        // Haal de ingelogde gebruiker op via de Principal
        Optional<User> currentUserOpt = userService.getUserByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User currentUser = currentUserOpt.get();

        try {
            Mp3FileDto savedFile = mp3FileService.saveFile(file, name, type, size, currentUser); // Geef de gebruiker door
            return ResponseEntity.ok(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<List<Mp3FileDto>> listFiles(Principal principal) {
        // Haal de ingelogde gebruiker op via de Principal en controleer of deze bestaat
        Optional<User> currentUserOpt = userService.getUserByEmail(principal.getName());
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Returneer 404 als de gebruiker niet wordt gevonden
        }

        User currentUser = currentUserOpt.get(); // "Unwrap" de gebruiker uit het Optional

        List<Mp3FileDto> files = mp3FileService.getAllFiles(currentUser); // Geef de ingelogde gebruiker door
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mp3FileDto> getFileById(@PathVariable Long id) {
        try {
            Mp3FileDto mp3FileDto = mp3FileService.getFile(id);
            return ResponseEntity.ok(mp3FileDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<Resource> playFile(@PathVariable Long id) throws MalformedURLException {
        Mp3File mp3File = mp3FileService.getFileEntity(id);

        Path filePath = Paths.get(mp3File.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            throw new RuntimeException("Het bestand kan niet worden gelezen.");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws MalformedURLException {
        Mp3File mp3File = mp3FileService.getFileEntity(id);

        mp3File.incrementDownloadCount();
        mp3FileService.save(mp3File);

        Path filePath = Paths.get(mp3File.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mp3File.getFileName() + ".mp3\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Het bestand kan niet worden gelezen.");
        }
    }

    @PutMapping("/playcount/{id}")
    public ResponseEntity<Void> incrementPlayCount(@PathVariable Long id) {
        try {
            mp3FileService.incrementPlayCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user-files/download-stats")
    public ResponseEntity<Map<String, Object>> getUserDownloadStats(Principal principal) {
        // Haal de ingelogde gebruiker op via de principal
        User user = userService.findByEmail(principal.getName());

        // Haal alle MP3-bestanden van de gebruiker op
        List<Mp3File> userFiles = mp3FileService.getFilesByUser(user.getId());

        // Bereken het totaal aantal downloads
        int totalDownloads = userFiles.stream()
                .mapToInt(Mp3File::getDownloadCount)
                .sum();

        // Bereid de response voor
        Map<String, Object> response = new HashMap<>();
        response.put("totalDownloads", totalDownloads);
        response.put("totalFiles", userFiles.size());

        return ResponseEntity.ok(response);
    }
}
