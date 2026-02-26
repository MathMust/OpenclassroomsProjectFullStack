package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié à la gestion des commentaires.
 *
 * <p>
 * Permet :
 * <ul>
 *     <li>La création d’un commentaire (utilisateur authentifié)</li>
 *     <li>La récupération de la liste des commentaires</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    /** Service métier gérant la logique liée aux commentaires. */
    public CommentService commentService;

    /**
     * Constructeur avec injection du service des commentaires.
     *
     * @param commentService service métier des commentaires
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Crée un nouveau commentaire associé à l’utilisateur authentifié.
     *
     * @param request données du commentaire
     * @param authentication utilisateur actuellement authentifié
     * @return message de confirmation
     */
    @PostMapping
    public ResponseEntity<?> create (@RequestBody CommentRequest request, Authentication authentication) {
        commentService.create(request, authentication);
            return ResponseEntity.ok().body("Comment created !");
    }

    /**
     * Retourne la liste complète des commentaires.
     *
     * @return liste des commentaires
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

}
