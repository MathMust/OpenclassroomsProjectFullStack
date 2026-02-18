package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    public CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> create (@RequestBody CommentRequest request, Authentication authentication) {
        commentService.create(request, authentication);
            return ResponseEntity.ok().body("Comment created !");
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

}
