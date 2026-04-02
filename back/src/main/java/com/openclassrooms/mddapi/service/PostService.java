package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.constants.Constants;
import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service métier responsable de la gestion des publications (posts).
 *
 * <p>
 * Gère :
 * <ul>
 *     <li>La création d’un post associé à un utilisateur et à un topic</li>
 *     <li>La récupération de tous les posts</li>
 *     <li>La récupération d’un post spécifique par son identifiant</li>
 * </ul>
 * </p>
 */
@Service
public class PostService {

    private final TopicService topicService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final PostRepository postRepository;

    /**
     * Constructeur avec injection des dépendances.
     */
    public PostService(TopicService topicService, PostMapper postMapper, UserService userService, PostRepository postRepository) {
        this.topicService = topicService;
        this.postMapper = postMapper;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    /**
     * Crée un nouveau post pour l’utilisateur authentifié et le topic spécifié.
     *
     * @param request données du post
     * @param authentication utilisateur actuellement authentifié
     */
    public void create(PostRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);

        Topic topic = topicService.getById(request.getTopicId());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(user);
        post.setTopic(topic);
        post.setDate(LocalDateTime.now());
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);
    }

    /**
     * Retourne tous les posts lié aux thèmes abonnés.
     *
     * @param authentication utilisateur actuellement authentifié
     * @return {@link PostsResponse} contenant la liste des posts
     */
    public PostsResponse getAll(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);

        List<Integer> subscribedTopicIds = Optional.ofNullable(user.getSubscriptions())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Subscription::getTopic)
                        .filter(Objects::nonNull)
                        .map(Topic::getId)
                        .filter(Objects::nonNull)
                        .toList();

        List<Post> posts = postRepository.findAllByTopicIdIn(subscribedTopicIds);
        List<PostDto> postDtos = postMapper.postListToPostDtoList(posts);
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setPosts(postDtos);

        return postsResponse;
    }

    /**
     * Retourne un post spécifique par son identifiant.
     *
     * @param id identifiant du post
     * @return {@link PostDto} correspondant
     * @throws IllegalArgumentException si le post n’existe pas
     */
    public PostDto getById(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return postMapper.postToPostDto(post.get());
        } else {
            throw new IllegalArgumentException(Constants.TOPIC_NOT_FOUND);
        }
    }
}
