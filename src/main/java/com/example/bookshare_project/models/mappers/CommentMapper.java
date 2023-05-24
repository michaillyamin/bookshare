package com.example.bookshare_project.models.mappers;

import com.example.bookshare_project.models.dto.CommentDto;
import com.example.bookshare_project.models.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "username", source = "comment.username")
    CommentDto commentToCommentDto(Comment comment);

    Comment commentDtoToComment(CommentDto commentDto);
}
