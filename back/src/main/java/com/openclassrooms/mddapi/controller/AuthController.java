package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthSuccess;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié à l’authentification et à la gestion du compte utilisateur.
 *
 * <p>
 * Expose les endpoints suivants :
 * <ul>
 *     <li>Inscription</li>
 *     <li>Connexion</li>
 *     <li>Déconnexion</li>
 *     <li>Récupération du profil utilisateur</li>
 *     <li>Mise à jour du compte</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les opérations d’authentification reposent sur un mécanisme JWT.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Service métier gérant la logique d’authentification. */
    public AuthService authService;

    /**
     * Constructeur avec injection du service d’authentification.
     *
     * @param authService service métier d’authentification
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Enregistre un nouvel utilisateur et retourne un JWT.
     *
     * @param request données d’inscription validées
     * @return token JWT encapsulé dans {@link AuthSuccess}
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
            String token = authService.register(request);
            return ResponseEntity.ok().body(new AuthSuccess(token));
    }

    /**
    * Authentifie un utilisateur existant et retourne un JWT.
    *
    * @param request données de connexion
     * @return token JWT encapsulé dans {@link AuthSuccess}
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthSuccess(token));
    }

    /**
     * Endpoint de déconnexion.
     *
     * <p>
     * En architecture stateless JWT, la déconnexion est gérée côté client
     * (suppression du token).
     * </p>
     *
     * @return message de confirmation
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("disconnected !");
    }

    /**
     * Retourne les informations du profil utilisateur authentifié.
     *
     * @param authentication contexte d’authentification courant
     * @return informations utilisateur
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        UserResponse userResponse = authService.me(authentication);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Met à jour les informations du compte utilisateur et retourne un nouveau JWT.
     *
     * @param request nouvelles données utilisateur validées
     * @param authentication utilisateur actuellement authentifié
     * @return nouveau token JWT encapsulé dans {@link AuthSuccess}
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody RegisterRequest request, Authentication authentication) {
        String token = authService.update(request, authentication);
        return ResponseEntity.ok().body(new AuthSuccess(token));
    }
}
