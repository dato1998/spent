package com.project.security.session.aop.service;

import com.project.exceptions.BadCredentialsException;
import com.project.security.session.models.Session;
import com.project.security.session.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Service
public class AopService {
    private final SessionRepository sessionRepository;

    public AopService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void beforeMethod() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String header = request.getHeader("authorization");
            if (header != null) {
                String token = header.substring(7);
                Session session = sessionRepository.findByToken(token);
                if (session == null) {
                    throw new BadCredentialsException("invalid token");
                }
                long lastUpdatedDate = session.getLastUpdatedDate();
                long currentTime = new Date().toInstant().getEpochSecond();
                if ((currentTime - lastUpdatedDate) > 1800) {
                    sessionRepository.deleteByToken(token);
                    throw new BadCredentialsException("sorry but your session is already exhausted, please log in again");
                } else {
                    Session updatedSession = new Session(session.getToken(), session.getCreatedDate(), currentTime, session.getUserId());
                    updatedSession.setId(session.getId());
                    sessionRepository.save(updatedSession);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
