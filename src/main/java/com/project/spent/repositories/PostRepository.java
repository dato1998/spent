package com.project.spent.repositories;

import com.project.spent.models.Post;
import com.project.spent.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIdIn(Set<String> ids);

    List<Post> findAllByUserIdAndEventTrueOrderByPostedAtDesc(Long userId, Pageable pageable);

    List<Post> findAllByUserIdAndEventFalseOrderByPostedAtDesc(Long userId, Pageable pageable);

    List<Post> findAllByOrderByPostedAtDesc(Pageable pageable);

    List<Post> findAllBySubscribedUsersOrderByPostedAtDesc(User user, Pageable pageable);

    List<Post> findAllByBookmarkedUsersOrderByPostedAtDesc(User user, Pageable pageable);
}
