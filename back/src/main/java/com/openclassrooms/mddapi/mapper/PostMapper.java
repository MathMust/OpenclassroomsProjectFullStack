package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface PostMapper {

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "topicTitle", source = "topic.title")
    @Mapping(target = "comments", source = "comments")
    PostDto postToPostDto(Post post);

    List<PostDto> postListToPostDtoList(List<Post> post);

}
