package com.example.bookshare_project.controllers;

import com.example.bookshare_project.models.dto.PostDto;
import com.example.bookshare_project.models.entities.Post;
import com.example.bookshare_project.models.mappers.PostMapper;
import com.example.bookshare_project.models.payload.response.MessageResponse;
import com.example.bookshare_project.services.PostService;
import com.example.bookshare_project.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping()
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto,
                                             BindingResult bindingResult,
                                             Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDto, principal);
        PostDto createdPost = postMapper.postToPostDto(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> postDtoList = postService.getAllPosts()
                .stream()
                .map(postMapper::postToPostDto)
                .toList();

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDto>> getAllPostsForUser(Principal principal) {
        List<PostDto> postDtoList = postService.getAllPostsForUser(principal)
                .stream()
                .map(postMapper::postToPostDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}")
        public ResponseEntity<PostDto> likePost(@PathVariable("postId") Long postId,
                @PathVariable("username") String username) {

        Post post = postService.likePost(postId, username);
        PostDto postDto = postMapper.postToPostDto(post);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") Long postId, Principal principal) {
        postService.deletePost(postId, principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
