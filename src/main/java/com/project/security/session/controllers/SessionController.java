package com.project.security.session.controllers;

import com.project.security.session.services.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/market/session")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @DeleteMapping("/logout")
    public ResponseEntity deleteSessionClient(@RequestHeader HttpHeaders headers) {
        sessionService.deleteSessionClient(headers);
        return new ResponseEntity(HttpStatus.OK);
    }
}
