package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service métier responsable de la gestion des abonnements aux topics.
 *
 * <p>
 * Permet :
 * <ul>
 *     <li>De s’abonner à un topic</li>
 *     <li>De se désabonner d’un topic</li>
 * </ul>
 * </p>
 */
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    /**
     * Constructeur avec injection des dépendances.
     */
    public SubscriptionService(SubscriptionRepository subscriptionRepository, TopicRepository topicRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    /**
     * Abonne l’utilisateur authentifié au topic spécifié.
     *
     * <p>
     * Si l’utilisateur est déjà abonné, l’opération est ignorée.
     * </p>
     *
     * @param authentication utilisateur actuellement authentifié
     * @param topicId identifiant du topic
     * @throws RuntimeException si le topic n’existe pas
     */
    public void subscribe(Authentication authentication, Integer topicId) {

        String email = authentication.getName();
        User user = userService.getByEmail(email);

        if (subscriptionRepository.existsByUserIdAndTopicId(user.getId(), topicId)) {
            return; // déjà abonné
        }

        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException(Constants.TOPIC_NOT_FOUND));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTopic(topic);

        subscriptionRepository.save(subscription);
    }

    /**
     * Désabonne l’utilisateur authentifié du topic spécifié.
     *
     * @param authentication utilisateur actuellement authentifié
     * @param topicId identifiant du topic
     */
    @Transactional
    public void unsubscribe(Authentication authentication, Integer topicId) {

        String email = authentication.getName();
        User user = userService.getByEmail(email);

        subscriptionRepository.deleteByUserIdAndTopicId(user.getId(), topicId);
    }

}
