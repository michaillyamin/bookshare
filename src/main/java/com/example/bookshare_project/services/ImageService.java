package com.example.bookshare_project.services;

import com.example.bookshare_project.models.entities.Image;
import com.example.bookshare_project.models.entities.Post;
import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.models.exceptions.ImageNotFoundException;
import com.example.bookshare_project.repositories.ImageRepository;
import com.example.bookshare_project.repositories.UserRepository;
import com.example.bookshare_project.utils.ImageUtils;
import com.example.bookshare_project.utils.PostUtils;
import com.example.bookshare_project.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        log.info("Uploading image profile to User {}", user.getUsername());

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(ImageUtils.compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());

        return imageRepository.save(image);
    }

    @Transactional
    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(PostUtils.toSinglePostCollector());

        Image image = new Image();
        image.setPostId(post.getId());
        image.setImageBytes(ImageUtils.compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        log.info("Uploading image to Post {}", post.getId());

        return imageRepository.save(image);
    }

    public Image getImageToUser(Principal principal) {
        User user = UserUtils.getUserByPrincipal(principal, userRepository);

        Image image = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(ImageUtils.decompressBytes(image.getImageBytes()));
        }

        return image;
    }

    public Image getImageToPost(Long postId) {
        Image image = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(ImageUtils.decompressBytes(image.getImageBytes()));
        }

        return image;
    }

}
