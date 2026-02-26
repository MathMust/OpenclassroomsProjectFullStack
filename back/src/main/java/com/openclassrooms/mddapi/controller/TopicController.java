package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicRequest;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié à la gestion des topics.
 *
 * <p>
 * Permet :
 * <ul>
 *     <li>La création d’un topic</li>
 *     <li>La récupération de la liste des topics</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    /** Service métier gérant la logique liée aux topics. */
    public TopicService topicService;

    /**
     * Constructeur avec injection du service des topics.
     *
     * @param topicService service métier des topics
     */
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Crée un nouveau topic.
     *
     * @param request données du topic
     * @return message de confirmation
     */
    @PostMapping
    public ResponseEntity<?> create (@RequestBody TopicRequest request) {
            topicService.create(request);
            return ResponseEntity.ok().body("Topic created !");
    }

    /**
     * Retourne la liste des topics disponibles.
     *
     * @param authentication utilisateur actuellement authentifié
     * @return liste des topics
     */
    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        return ResponseEntity.ok(topicService.getAll(authentication));
    }

}
