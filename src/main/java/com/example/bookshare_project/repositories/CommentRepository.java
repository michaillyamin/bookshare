package com.example.bookshare_project.repositories;

import com.example.bookshare_project.models.entities.Comment;
import com.example.bookshare_project.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

}
