package com.dashboard.backend.service;

import com.dashboard.backend.dto.CommentDto;
import com.dashboard.backend.model.Comment;
import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.CommentRepository;
import com.dashboard.backend.repository.Mp3FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Mp3FileRepository mp3FileRepository;

    public CommentDto addComment(Long fileId, String content, User user) {

        Role userRole = user.getRole();

        if (userRole == null || !"ROLE_ADMIN".equals(userRole.getRoleName())) {
            throw new RuntimeException("Unauthorized");
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

        List<Comment> comments = commentRepository.findByMp3FileId(fileId);

        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        String artistName = comment.getUser().getArtistName();
        System.out.println("Artist name in Comment: " + artistName);
        dto.setUserName(artistName);
        dto.setFileId(comment.getMp3File().getId());
        return dto;
    }
}
