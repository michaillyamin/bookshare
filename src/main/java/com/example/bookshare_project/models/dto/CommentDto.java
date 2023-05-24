package com.example.bookshare_project.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty(message = "message should not be empty")
    private String message;

    private String username;
}
