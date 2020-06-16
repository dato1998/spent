package com.project.security.session.repositories;

import com.project.security.session.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByToken(String token);
    Session findByUserId(Long id);
    void deleteByToken(String token);
}
