package com.example.bookshare_project.models.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private String bookCaption;
    private String username;
    private Integer likes;
    private Set<String> usersLiked;

}
