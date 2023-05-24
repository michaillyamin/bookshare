package com.example.bookshare_project.repositories;

import com.example.bookshare_project.models.entities.Post;
import com.example.bookshare_project.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findPostByIdAndUser(Long postId, User user);
}
