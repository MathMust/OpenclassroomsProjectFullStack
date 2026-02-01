package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topic")
public class SubscriptionController {

    public SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Integer id, Authentication authentication) {
        subscriptionService.subscribe(authentication, id);
        return ResponseEntity.ok().body("Subscription completed !");
    }

    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Integer id, Authentication authentication) {
        subscriptionService.unsubscribe(authentication, id);
        return ResponseEntity.ok().body("Unsubscription completed !");
    }
    
}
