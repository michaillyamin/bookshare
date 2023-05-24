package com.example.bookshare_project.models.mappers;

import com.example.bookshare_project.models.dto.PostDto;
import com.example.bookshare_project.models.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "username", source = "post.user.username")
    @Mapping(target = "usersLiked", source = "post.likedUsers")
    PostDto postToPostDto(Post post);

    Post postDtoToPost(PostDto postDto);
}
