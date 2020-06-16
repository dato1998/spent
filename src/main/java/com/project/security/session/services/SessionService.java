package com.project.security.session.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.security.session.models.Session;
import com.project.security.session.repositories.SessionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void deleteSessionClient(HttpHeaders headers) {
        String token = getToken(headers);
        Session session = sessionRepository.findByToken(token);
        if (session == null) {
            throw new EntityNotFoundException("session client was not find for given token ");
        }
        sessionRepository.deleteByToken(token);
    }

    private String getToken(HttpHeaders headers) {
        String token = null;
        if (headers != null && headers.get("Authorization") != null) {
            String bearerToken = Objects.requireNonNull(headers.get("Authorization")).toString();
            token = bearerToken.substring(8, bearerToken.length() - 1);
        }
        return token;
    }
}
