package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    boolean existsByUserIdAndTopicId(Integer userId, Integer topicId);

    void deleteByUserIdAndTopicId(Integer userId, Integer topicId);
}
