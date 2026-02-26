package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.TopicRequest;
import com.openclassrooms.mddapi.dto.TopicsResponse;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service métier responsable de la gestion des topics.
 *
 * <p>
 * Gère :
 * <ul>
 *     <li>La création d’un topic</li>
 *     <li>La récupération de tous les topics pour un utilisateur donné</li>
 *     <li>La récupération d’un topic spécifique</li>
 *     <li>La récupération de plusieurs topics par ID pour un utilisateur</li>
 * </ul>
 * </p>
 */
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final UserService userService;

    /**
     * Constructeur avec injection des dépendances.
     */
    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper, UserService userService) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.userService = userService;
    }

    /**
     * Crée un nouveau topic.
     *
     * @param request données du topic
     */
    public void create(TopicRequest request) {

        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());
        topic.setCreatedAt(LocalDateTime.now());

        topicRepository.save(topic);
    }

    /**
     * Retourne tous les topics et indique pour l’utilisateur
     * authentifié s’il est abonné à chacun.
     *
     * @param authentication utilisateur actuellement authentifié
     * @return {@link TopicsResponse} contenant la liste des topics
     */
    public TopicsResponse getAll(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        List<Topic> topics = topicRepository.findAll();
        List<TopicDto> topicDtos = topicMapper.topicListToTopicDtoList(topics, user.getId());
        TopicsResponse topicsResponse = new TopicsResponse();
        topicsResponse.setTopics(topicDtos);

        return topicsResponse;
    }

    /**
     * Retourne un topic spécifique par son identifiant.
     *
     * @param id identifiant du topic
     * @return {@link Topic} correspondant
     * @throws RuntimeException si le topic n’existe pas
     */
    public Topic getById(Integer id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.TOPIC_NOT_FOUND));
    }

    /**
     * Retourne une liste de TopicDto pour les topics spécifiés
     * et indique si l’utilisateur est abonné à chacun.
     *
     * @param ids liste des identifiants des topics
     * @param userId identifiant de l’utilisateur
     * @return liste de {@link TopicDto}
     */
    public List<TopicDto> getByIdAndUserId(List<Integer> ids, Integer userId) {
        List<Topic> topics = topicRepository.findAllById(ids);
        return topicMapper.topicListToTopicDtoList(topics, userId);
    }
}
