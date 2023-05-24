package com.example.bookshare_project.controllers;

import com.example.bookshare_project.models.entities.Image;
import com.example.bookshare_project.models.payload.response.MessageResponse;
import com.example.bookshare_project.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService imageUploadService;

    @PostMapping()
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded to User Successfully"));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") Long postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToPost(file, principal, postId);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded to Post Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Image> getImageForUser(Principal principal) {
        Image userImage = imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") Long postId) {
        Image postImage = imageUploadService.getImageToPost(postId);
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
