package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié à la gestion des abonnements aux topics.
 *
 * <p>
 * Permet à un utilisateur authentifié :
 * <ul>
 *     <li>De s’abonner à un topic</li>
 *     <li>De se désabonner d’un topic</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/topic")
public class SubscriptionController {

    /** Service métier gérant la logique d’abonnement aux topics. */
    public SubscriptionService subscriptionService;

    /**
     * Constructeur avec injection du service d’abonnement.
     *
     * @param subscriptionService service métier des abonnements
     */
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Abonne l’utilisateur authentifié au topic spécifié.
     *
     * @param id identifiant du topic
     * @param authentication utilisateur actuellement authentifié
     * @return message de confirmation
     */
    @GetMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Integer id, Authentication authentication) {
        subscriptionService.subscribe(authentication, id);
        return ResponseEntity.ok().body("Subscription completed !");
    }

    /**
     * Désabonne l’utilisateur authentifié du topic spécifié.
     *
     * @param id identifiant du topic
     * @param authentication utilisateur actuellement authentifié
     * @return message de confirmation
     */
    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Integer id, Authentication authentication) {
        subscriptionService.unsubscribe(authentication, id);
        return ResponseEntity.ok().body("Unsubscription completed !");
    }
    
}
