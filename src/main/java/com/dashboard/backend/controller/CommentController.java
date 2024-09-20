package com.dashboard.backend.controller;

import com.dashboard.backend.dto.CommentDto;
import com.dashboard.backend.model.User;
import com.dashboard.backend.service.CommentService;
import com.dashboard.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/files")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;


    // Admins kunnen comments plaatsen
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long id,
                                                 @RequestBody CommentDto commentDto,
                                                 Principal principal) {
        // Haal de ingelogde gebruiker op via de Principal
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        // Controleer of de gebruiker een admin is
        if (!user.getRole().getRoleName().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Alleen admins mogen comments plaatsen
        }

        // Voeg de comment toe
        CommentDto addedComment = commentService.addComment(id, commentDto.getContent(), user);
        return ResponseEntity.ok(addedComment);
    }

    // Gebruikers kunnen comments zien
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        List<CommentDto> comments = commentService.getCommentsForFile(id, user);
        return ResponseEntity.ok(comments);
    }
}
