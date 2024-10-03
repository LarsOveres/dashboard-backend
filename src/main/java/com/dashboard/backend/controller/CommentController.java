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

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long id,
                                                 @RequestBody CommentDto commentDto,
                                                 Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

            System.out.println("Rol van gebruiker: " + user.getRole().getRoleName());

            if (!user.getRole().getRoleName().equals("ROLE_ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            if (commentDto.getContent() == null || commentDto.getContent().trim().isEmpty()) {
                CommentDto errorComment = new CommentDto();
                errorComment.setContent("Comment moet minimaal 1 karakter bevatten");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorComment);
            }

            CommentDto addedComment = commentService.addComment(id, commentDto.getContent(), user);
            return ResponseEntity.ok(addedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        List<CommentDto> comments = commentService.getCommentsForFile(id, user);
        return ResponseEntity.ok(comments);
    }
}
