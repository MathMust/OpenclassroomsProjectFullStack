package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicRequest;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topic")
public class TopicController {

    public TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<?> create (@ModelAttribute TopicRequest request) {
            topicService.create(request);
            return ResponseEntity.ok().body("Topic created !");
    }

    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        return ResponseEntity.ok(topicService.getAll(authentication));
    }

}
