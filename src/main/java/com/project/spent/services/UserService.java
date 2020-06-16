package com.project.spent.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.security.configs.JwtTokenUtil;
import com.project.security.service.SecurityService;
import com.project.spent.dtos.Credentials;
import com.project.spent.dtos.UserDTO;
import com.project.spent.models.User;
import com.project.spent.repositories.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityService securityService;
    private final SendGrid sendGrid;
    private final ModelMapper mapper;

    public UserService(UserRepository repository, JwtTokenUtil jwtTokenUtil, SecurityService securityService, SendGrid sendGrid, ModelMapper mapper) {
        this.repository = repository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.securityService = securityService;
        this.sendGrid = sendGrid;
        this.mapper = mapper;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public UserDTO add(UserDTO dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        User user = repository.save(dtoToEntity(dto));
        return entityToDto(user);
    }

    @Transactional
    public void update(Long id, UserDTO dto) {
        User oldUser = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id : " + id));
        User user = dtoToEntity(dto);
        user.setId(id);
        if (!dto.getPassword().equals(oldUser.getPassword())) {
            user.setPassword(encoder.encode(dto.getPassword()));
        } else {
            user.setPassword(oldUser.getPassword());
        }
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDTO get(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id : " + id));
        return entityToDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAll() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> forgotPassword(Credentials dto) {
        Map<String, Object> messageForForgotPassword = new HashMap<>();
        User existingUser = repository.findByEmail(dto.getEmail());
        if (existingUser != null) {
            // create token
            String token = jwtTokenUtil.generateToken(securityService.loadUserByUsername(existingUser.getEmail()));
            // save it
            // create the email
            Email from = new Email("dmartyoflishvili@gmail.com");
            String subject = "Complete Password Reset!";
            Email to = new Email(existingUser.getEmail());
            Content content = new Content("text/plain", "To complete the password reset process, please click here: "
                    + "heroku link" + token);
            Mail mail = new Mail(from, subject, to, content);

            mail.setReplyTo(new Email("dmartyoflishvili@gmail.com"));
//            mail.personalization.get(0).addSubstitution("-username-", "Some blog user");
//            mail.setTemplateId(emailTemplateId);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sendGrid.api(request);
                messageForForgotPassword.put("message", response);
            } catch (IOException ex) {
                throw new EntityNotFoundException("cant get response please check mail or endpoint");
            }
            return messageForForgotPassword;
        } else {
            throw new EntityNotFoundException("this email does not exist!");
        }
    }

    @Transactional
    public Map<String, Object> resetPassword(String confirmationToken, Credentials dto) {
        Map<String, Object> messageForResetPassword = new HashMap<>();
        String email = jwtTokenUtil.getEmailFromToken(confirmationToken);
        User user = repository.findByEmail(email);
        user.setPassword(encoder.encode(dto.getPassword()));
        repository.save(user);
        messageForResetPassword.put("message", "Password successfully reset. You can now log in with the new credentials.");
        return messageForResetPassword;
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id : " + id));
        repository.deleteById(id);
    }

    private UserDTO entityToDto(User user) {
        return mapper.map(user, UserDTO.class);
    }

    private User dtoToEntity(UserDTO dto) {
        return mapper.map(dto, User.class);
    }
}
