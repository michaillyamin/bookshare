package com.example.bookshare_project.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotEmpty(message = "Firstname should not be empty")
    private String name;

    private String username;

    @NotEmpty(message = "Lastname should not be empty")
    private String lastname;

    private String bio;

}
