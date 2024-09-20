package com.dashboard.backend.service;

import com.dashboard.backend.dto.CommentDto;
import com.dashboard.backend.model.Comment;
import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.CommentRepository;
import com.dashboard.backend.repository.Mp3FileRepository;
import com.dashboard.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mp3FileRepository mp3FileRepository;

    public CommentDto addComment(Long fileId, String content, User user) {
        // Verkrijg de rol van de gebruiker
        Role userRole = user.getRole();

        // Controleer of de gebruiker een admin is
        if (userRole == null || !"ADMIN".equals(userRole.getRoleName())) {
            throw new RuntimeException("Unauthorized"); // Alleen admins mogen comments toevoegen
        }

        Mp3File file = mp3FileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Comment comment = new Comment();
        comment.setMp3File(file);
        comment.setUser(user);
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    public List<CommentDto> getCommentsForFile(Long fileId, User user) {
        // Alle gebruikers kunnen alle comments zien
        List<Comment> comments = commentRepository.findByMp3FileId(fileId);

        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        String artistName = comment.getUser().getArtistName();  // Haal de artistName op
        System.out.println("Artist name in Comment: " + artistName);  // Log de artistName voor debugging
        dto.setUserName(artistName);  // Zet de artistName in de DTO
        dto.setFileId(comment.getMp3File().getId());
        return dto;
    }
}
