package com.example.bookshare_project.models.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ProblemDetail;

@Data
@AllArgsConstructor
public class MessageResponse extends ProblemDetail {
    private String message;
}
