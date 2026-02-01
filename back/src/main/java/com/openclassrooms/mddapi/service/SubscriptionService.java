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

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, TopicRepository topicRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

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

    @Transactional
    public void unsubscribe(Authentication authentication, Integer topicId) {

        String email = authentication.getName();
        User user = userService.getByEmail(email);

        subscriptionRepository.deleteByUserIdAndTopicId(user.getId(), topicId);
    }

}
