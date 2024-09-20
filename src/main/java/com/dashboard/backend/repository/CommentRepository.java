package com.dashboard.backend.repository;

import com.dashboard.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMp3FileId(Long mp3FileId);
    List<Comment> findByMp3FileIdAndUserId(Long mp3FileId, Long userId);
}

