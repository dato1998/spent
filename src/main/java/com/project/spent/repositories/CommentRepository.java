package com.project.spent.repositories;

import com.project.spent.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByPostIdOrderByCommentedAtDesc(Long postId, Pageable pageable);

    List<Comment> findAllByParentId(Long parentId);

    List<Comment> findAllByParentIsNull();

    long countByParentId(Long parentId);

    long countByPostId(Long postId);
}
