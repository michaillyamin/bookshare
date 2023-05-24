package com.example.bookshare_project.services;

import com.example.bookshare_project.models.dto.CommentDto;
import com.example.bookshare_project.models.entities.Comment;
import com.example.bookshare_project.models.entities.Post;
import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.models.exceptions.PostNotFoundException;
import com.example.bookshare_project.models.mappers.CommentMapper;
import com.example.bookshare_project.repositories.CommentRepository;
import com.example.bookshare_project.repositories.PostRepository;
import com.example.bookshare_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Comment saveComment(Long postId, CommentDto commentDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));

        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());

        log.info("Saving comment form Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        return commentRepository.findAllByPost(post);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %s not found", username)));
    }
}
