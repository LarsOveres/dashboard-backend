package com.dashboard.backend.service;

import com.dashboard.backend.dto.Mp3FileDto;
import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.Mp3FileRepository;
import com.dashboard.backend.repository.UserRepository;
import com.dashboard.backend.util.Mp3DurationUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class Mp3FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void logConfig() {
        System.out.println("Bestanden worden opgeslagen in: " + uploadDir);
    }

    private final Mp3FileRepository mp3FileRepository;
    private final UserRepository userRepository;

    @Autowired
    public Mp3FileService(Mp3FileRepository mp3FileRepository, UserRepository userRepository) {
        this.mp3FileRepository = mp3FileRepository;
        this.userRepository = userRepository;
    }

    public Mp3FileDto saveFile(MultipartFile file, String name, String type, Long size, User currentUser) throws IOException {
        System.out.println("Starten van bestand upload");

        if (file.isEmpty()) {
            System.err.println("Fout: Het bestand is leeg!");
            throw new IllegalArgumentException("Het bestand is leeg!");
        }

        // Bepaal het pad voor uploads relatief aan de root van het project
        Path uploadPath = Paths.get("uploads").toAbsolutePath();
        File dir = uploadPath.toFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Fout: Kan de directory niet aanmaken: " + uploadPath);
                throw new IOException("Kan de directory niet aanmaken: " + uploadPath);
            }
        }

        // Controleer of de gebruiker geldig is
        if (currentUser == null || currentUser.getId() == null) {
            System.err.println("Fout: De gebruiker is niet geldig!");
            throw new IllegalArgumentException("De gebruiker is niet geldig!");
        }

        // Maak een tijdelijke Mp3File instantie aan zonder ID
        Mp3File mp3File = new Mp3File();
        mp3File.setFileName(name);
        mp3File.setFileType(type);
        mp3File.setFileSize(size);
        mp3File.setUser(currentUser);

        // Bewaar het bestand tijdelijk in een bestand met een dummy ID
        String tempFilePath = uploadPath.resolve("temp.mp3").toString();
        File dest = new File(tempFilePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            System.err.println("Fout bij het opslaan van het bestand naar tijdelijke locatie: " + e.getMessage());
            throw e;
        }

        // Verkrijg de duur van het MP3-bestand
        long durationInSeconds;
        try {
            durationInSeconds = Mp3DurationUtil.getMp3Duration(dest);
            System.out.println("MP3 duur in seconden: " + durationInSeconds);
        } catch (Exception e) {
            System.err.println("Fout bij het verkrijgen van de duur van het MP3-bestand: " + e.getMessage());
            throw e;
        }

        mp3File.setFilePath(tempFilePath);
        mp3File.setDuration(durationInSeconds);

        // Sla de Mp3File entity op in de database
        try {
            mp3File = mp3FileRepository.save(mp3File);
        } catch (Exception e) {
            System.err.println("Fout bij het opslaan van Mp3File in de database: " + e.getMessage());
            throw e;
        }

        // Verander het pad naar de definitieve locatie
        String finalFilePath = uploadPath.resolve(mp3File.getId() + ".mp3").toString();
        File finalDest = new File(finalFilePath);
        if (!dest.renameTo(finalDest)) {
            System.err.println("Fout bij het hernoemen van het bestand naar: " + finalFilePath);
            throw new IOException("Fout bij het hernoemen van het bestand.");
        }

        mp3File.setFilePath(finalFilePath);
        try {
            mp3FileRepository.save(mp3File);
        } catch (Exception e) {
            System.err.println("Fout bij het opnieuw opslaan van Mp3File met definitieve pad: " + e.getMessage());
            throw e;
        }

        // Converteer de entity naar een DTO
        Mp3FileDto mp3FileDto = new Mp3FileDto();
        mp3FileDto.setFileName(mp3File.getFileName());
        mp3FileDto.setFilePath(mp3File.getFilePath());
        mp3FileDto.setFileType(mp3File.getFileType());
        mp3FileDto.setFileSize(mp3File.getFileSize());
        mp3FileDto.setDuration(mp3File.getDuration());
        mp3FileDto.setArtistName(currentUser.getArtistName());

        return mp3FileDto;
    }


    public List<Mp3FileDto> getAllFiles(User user) {
        List<Mp3File> files;
        if (user.getRole() != null && user.getRole().getRoleName().equals("ADMIN")) {
            files = mp3FileRepository.findAll(); // Admin kan alle bestanden zien
        } else {
            files = mp3FileRepository.findByUserId(user.getId()); // Gewone gebruiker kan alleen hun eigen bestanden zien
        }

        return files.stream().map(file -> {
            Mp3FileDto dto = new Mp3FileDto();
            dto.setId(file.getId());
            dto.setFileName(file.getFileName());
            dto.setFilePath(file.getFilePath());
            dto.setArtistName(file.getUser() != null ? file.getUser().getArtistName() : "Onbekend");
            return dto;
        }).collect(Collectors.toList());
    }

    public Mp3FileDto getFile(Long id) {
        Mp3File mp3File = mp3FileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("File not found"));

        String fileName = mp3File.getFileName();
        String filePath = mp3File.getFilePath();
        User user = mp3File.getUser();
        String artistName = user != null ? user.getArtistName() : "Onbekend";
        Long fileSize = mp3File.getFileSize();
        String fileType = mp3File.getFileType();
        int playCount = mp3File.getPlayCount();
        int downloadCount = mp3File.getDownloadCount();

        return new Mp3FileDto(id, fileName, filePath, user, artistName, fileSize, fileType, playCount, downloadCount);
    }

    public Mp3File getFileEntity(Long id) {
        return mp3FileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bestand niet gevonden"));
    }

    public static long getMp3Duration(File mp3File) throws IOException {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(mp3File);
            Map<String, Object> properties = fileFormat.properties();
            Long duration = (Long) properties.get("duration");

            // Converteer duur naar seconden (duurtijd wordt in microseconden gegeven)
            return duration / 1_000_000;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IOException("Kan de MP3 duur niet bepalen: " + e.getMessage());
        }
    }

    public Mp3File updateFile(Mp3File file) {
        return mp3FileRepository.save(file);
    }

    public void incrementPlayCount(Long id) {
        Mp3File mp3File = mp3FileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("File not found"));

        mp3File.setPlayCount(mp3File.getPlayCount() + 1);
        mp3FileRepository.save(mp3File);
    }

    public Mp3File save(Mp3File mp3File) {
        return mp3FileRepository.save(mp3File);
    }

    public List<Mp3File> getFilesByUser(Long userId) {
        return mp3FileRepository.findByUserId(userId);
    }
}
