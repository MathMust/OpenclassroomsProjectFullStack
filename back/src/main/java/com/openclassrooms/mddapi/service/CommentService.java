package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentsResponse;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(CommentMapper commentMapper, UserService userService, PostRepository postRepository, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void create(CommentRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);

        Post post = postRepository.getReferenceById(request.getPostId());

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setDate(LocalDateTime.now());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
    }

    public CommentsResponse getAll() {
        List<Comment> comments = commentRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        List<CommentDto> commentDtos = commentMapper.commentListToCommentDtoList(comments);
        CommentsResponse commentsResponse = new CommentsResponse();
        commentsResponse.setComments(commentDtos);

        return commentsResponse;
    }

}
