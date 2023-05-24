package com.example.bookshare_project.models.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    private String email;

    @NotEmpty(message = "Firstname is required")
    private String firstname;

    @NotEmpty(message = "Lastname is required")
    private String lastname;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password should be bigger than 6 symbols")
    private String password;

    private String confirmPassword;
}
