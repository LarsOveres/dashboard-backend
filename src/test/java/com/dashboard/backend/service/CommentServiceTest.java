package com.dashboard.backend.service;

import com.dashboard.backend.dto.CommentDto;
import com.dashboard.backend.model.Comment;
import com.dashboard.backend.model.Mp3File;
import com.dashboard.backend.model.Role;
import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.CommentRepository;
import com.dashboard.backend.repository.Mp3FileRepository;
import com.dashboard.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mp3FileRepository mp3FileRepository;

    private User adminUser;
    private User regularUser;
    private Mp3File mp3File;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");

        Role userRole = new Role();
        userRole.setRoleName("USER");

        adminUser = new User();
        adminUser.setRole(adminRole);
        adminUser.setArtistName("admin gebruiker");

        regularUser = new User();
        regularUser.setRole(userRole);
        regularUser.setArtistName("gebruiker");

        mp3File = new Mp3File();
        mp3File.setId(1L);
    }

    @Test
    void shouldAddCommentWhenUserIsAdmin() {

        Long fileId = 1L;
        String commentContent = "Goed nummer!";

        when(mp3FileRepository.findById(fileId)).thenReturn(Optional.of(mp3File));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDto result = commentService.addComment(fileId, commentContent, adminUser);

        assertNotNull(result);
        assertEquals(commentContent, result.getContent());
        assertEquals(adminUser.getArtistName(), result.getUserName());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {

        Long fileId = 1L;
        String commentContent = "Goed nummer!";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.addComment(fileId, commentContent, regularUser);
        });

        assertEquals("Unauthorized", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {

        Long fileId = 1L;
        String commentContent = "Goed nummer!";

        when(mp3FileRepository.findById(fileId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.addComment(fileId, commentContent, adminUser);
        });

        assertEquals("File not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void shouldGetCommentsForFile() {

        Long fileId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Goed nummer!");
        comment.setUser(adminUser);
        comment.setMp3File(mp3File);

        when(commentRepository.findByMp3FileId(fileId)).thenReturn(Collections.singletonList(comment));

        List<CommentDto> result = commentService.getCommentsForFile(fileId, adminUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(comment.getContent(), result.get(0).getContent());
        assertEquals(adminUser.getArtistName(), result.get(0).getUserName());
        verify(commentRepository, times(1)).findByMp3FileId(fileId);
    }
}
