package com.dashboard.backend.service;

import com.dashboard.backend.dto.Mp3FileDto;
import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.Mp3FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class Mp3FileService {

    private final Mp3FileRepository mp3FileRepository;

    @Autowired
    public Mp3FileService(Mp3FileRepository mp3FileRepository) {
        this.mp3FileRepository = mp3FileRepository;
    }

    public Mp3FileDto saveFile(MultipartFile file, String name, String type, Long size, User user) throws IOException {
        String currentWorkingDir = System.getProperty("user.dir");
        System.out.println("Huidige werkdirectory: " + currentWorkingDir);
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Het bestand is leeg!");
        }

        // Definieer de locatie waar je het bestand wilt opslaan
        String uploadDir = "E:\\NOVI\\EINDOPDRACHT NOVI\\Broncode_Backend\\dashboard_backend\\uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Sla het bestand op in de directory
        String filePath = uploadDir + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);

        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("De gebruiker is niet geldig!");
        }

        // Sla de file informatie op in de database
        Mp3File mp3File = new Mp3File();
        mp3File.setFileName(name);
        mp3File.setFilePath(filePath);
        mp3File.setFileType(type);
        mp3File.setFileSize(size);
        mp3File.setUser(user);

        mp3FileRepository.save(mp3File);

        // Converteer de entity naar een DTO
        Mp3FileDto mp3FileDto = new Mp3FileDto();
        mp3FileDto.setFileName(mp3File.getFileName());
        mp3FileDto.setFilePath(mp3File.getFilePath());
        mp3FileDto.setFileType(mp3File.getFileType());
        mp3FileDto.setFileSize(mp3File.getFileSize());
        mp3FileDto.setUser(mp3File.getUser());

        return mp3FileDto;
    }
}
