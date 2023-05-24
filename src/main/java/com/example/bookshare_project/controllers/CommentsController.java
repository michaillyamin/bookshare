package com.example.bookshare_project.controllers;

import com.example.bookshare_project.models.dto.CommentDto;
import com.example.bookshare_project.models.entities.Comment;
import com.example.bookshare_project.models.mappers.CommentMapper;
import com.example.bookshare_project.models.payload.response.MessageResponse;
import com.example.bookshare_project.services.CommentService;
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
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable("postId") Long postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(postId, commentDto, principal);
        CommentDto createdComment = commentMapper.commentToCommentDto(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable("postId") Long postId) {
        List<CommentDto> commentDtoList = commentService.getAllCommentsForPost(postId)
                .stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }
}
