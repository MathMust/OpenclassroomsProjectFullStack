package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    public PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> create (@ModelAttribute PostRequest request, Authentication authentication) {
            postService.create(request, authentication);
            return ResponseEntity.ok().body("Post created !");
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getById(id));
    }

}
