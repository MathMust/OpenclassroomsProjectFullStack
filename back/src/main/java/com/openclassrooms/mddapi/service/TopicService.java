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

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final UserService userService;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper, UserService userService) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.userService = userService;
    }

    public void create(TopicRequest request) {

        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());
        topic.setCreatedAt(LocalDateTime.now());

        topicRepository.save(topic);
    }

    public TopicsResponse getAll(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        List<Topic> topics = topicRepository.findAll();
        List<TopicDto> topicDtos = topicMapper.topicListToTopicDtoList(topics, user.getId());
        TopicsResponse topicsResponse = new TopicsResponse();
        topicsResponse.setTopics(topicDtos);

        return topicsResponse;
    }

    public Topic getById(Integer id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.TOPIC_NOT_FOUND));
    }

    public List<TopicDto> getByIdAndUserId(List<Integer> ids, Integer userId) {
        List<Topic> topics = topicRepository.findAllById(ids);
        return topicMapper.topicListToTopicDtoList(topics, userId);
    }
}
