package com.example.bookshare_project.services;

import com.example.bookshare_project.models.dto.PostDto;
import com.example.bookshare_project.models.entities.Image;
import com.example.bookshare_project.models.entities.Post;
import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.models.exceptions.PostNotFoundException;
import com.example.bookshare_project.models.mappers.PostMapper;
import com.example.bookshare_project.repositories.ImageRepository;
import com.example.bookshare_project.repositories.PostRepository;
import com.example.bookshare_project.repositories.UserRepository;
import com.example.bookshare_project.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final ImageRepository imageRepository;

    @Transactional
    public Post createPost(PostDto postDto, Principal principal) {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        Post post = postMapper.postDtoToPost(postDto);
        post.setUser(user);

        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        return postRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(user -> user.equals(username))
                .findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            if (post.getLikes() == null) {
                post.setLikes(1);
            } else {
                post.setLikes(post.getLikes() + 1);
            }
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<Image> image = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        image.ifPresent(imageRepository::delete);
    }

}
