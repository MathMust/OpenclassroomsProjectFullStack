package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    @Mapping(target = "subscription", source = "topic", qualifiedByName = "isSubscribed")
    TopicDto topicToTopicDto(Topic topic, @Context Integer userId);

    List<TopicDto> topicListToTopicDtoList(List<Topic> topics, @Context Integer userId);

    @Named("isSubscribed")
    default boolean isSubscribed(Topic topic, @Context Integer userId) {
        return topic.getSubscriptions() != null &&
                topic.getSubscriptions().stream()
                        .anyMatch(sub ->
                                sub.getUser() != null &&
                                        sub.getUser().getId().equals(userId)
                        );
    }
}
