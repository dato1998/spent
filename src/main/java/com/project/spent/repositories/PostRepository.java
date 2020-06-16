package com.project.spent.repositories;

import com.project.spent.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIdIn(Set<String> ids);
    List<Post> findAllByUserId(Long userId);
}
