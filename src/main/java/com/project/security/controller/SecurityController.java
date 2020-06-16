package com.project.security.controller;

import com.project.security.model.ClientInformation;
import com.project.security.model.JwtRequest;
import com.project.security.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/security")
public class SecurityController {
    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/login")
    public ResponseEntity<ClientInformation> createLoginToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return new ResponseEntity<>(securityService.login(authenticationRequest), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ClientInformation> isTokenActive(final String token) {
        return new ResponseEntity<>(securityService.isTokenActive(token), HttpStatus.OK);
    }
}