package com.project.security.service;

import com.project.exceptions.BadCredentialsException;
import com.project.files.services.CloudinaryStorageService;
import com.project.security.configs.JwtTokenUtil;
import com.project.security.model.ClientInformation;
import com.project.security.model.JwtRequest;
import com.project.security.session.models.Session;
import com.project.security.session.repositories.SessionRepository;
import com.project.spent.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Service
public class SecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final SessionRepository sessionRepository;
    private final CloudinaryStorageService cloudinaryStorageService;

    public SecurityService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                           SessionRepository sessionRepository, CloudinaryStorageService cloudinaryStorageService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sessionRepository = sessionRepository;
        this.cloudinaryStorageService = cloudinaryStorageService;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.project.spent.models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public ClientInformation isTokenActive(final String token) {
        ClientInformation clientInformation = new ClientInformation();
        Boolean isExpired = jwtTokenUtil.isTokenExpired(token);
        if (!isExpired) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            com.project.spent.models.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            clientInformation.setUsername(user.getUsername());
            clientInformation.setFullName(user.getFullName());
            clientInformation.setEmail(user.getEmail());
            clientInformation.setUserId(userId);
            clientInformation.setToken(token);
        }
        return clientInformation;
    }

    @Transactional
    public ClientInformation login(JwtRequest authenticationRequest) throws Exception {
        com.project.spent.models.User user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("invalid credentials"));

        String password = user.getPassword();
        if (encoder.matches(authenticationRequest.getPassword(), password)) {
            return getResponseEntity(authenticationRequest);
        } else {
            throw new BadCredentialsException("invalid credentials");
        }
    }

    private ClientInformation getResponseEntity(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Session session = sessionRepository.findByUserId(userId);
        if (session == null) {
            sessionRepository.save(new Session(token, new Date(), new Date().toInstant().getEpochSecond(), userId));
        } else {
            Session updatedSession = new Session(token, session.getCreatedDate(), new Date().toInstant().getEpochSecond(), userId);
            updatedSession.setId(session.getId());
            sessionRepository.save(updatedSession);
        }
        String username = userDetails.getUsername();
        com.project.spent.models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String fileAsString = null;
        if (user.getPhoto() != null) {
            byte[] fileAsBytes = cloudinaryStorageService.getFile(user.getPhoto().getPath());
            fileAsString = Base64.getEncoder().encodeToString(fileAsBytes);
        }

        return new ClientInformation(user.getUsername(), user.getFullName(), user.getEmail(), userId, token, fileAsString);
    }

    private void authenticate(String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
