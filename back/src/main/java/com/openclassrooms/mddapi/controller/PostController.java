package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié à la gestion des publications (posts).
 *
 * <p>
 * Permet :
 * <ul>
 *     <li>La création d’un post (utilisateur authentifié)</li>
 *     <li>La récupération de tous les posts</li>
 *     <li>La récupération d’un post par identifiant</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/post")
public class PostController {

    /** Service métier gérant la logique liée aux publications. */
    public PostService postService;

    /**
     * Constructeur avec injection du service des publications.
     *
     * @param postService service métier des posts
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Crée un nouveau post associé à l’utilisateur authentifié.
     *
     * @param request données du post
     * @param authentication utilisateur actuellement authentifié
     * @return message de confirmation
     */
    @PostMapping
    public ResponseEntity<?> create (@RequestBody PostRequest request, Authentication authentication) {
            postService.create(request, authentication);
            return ResponseEntity.ok().body("Post created !");
    }

    /**
     * Retourne la liste complète des posts.
     *
     * @return liste des publications
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    /**
     * Retourne un post spécifique par son identifiant.
     *
     * @param id identifiant du post
     * @return post correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getById(id));
    }

}
