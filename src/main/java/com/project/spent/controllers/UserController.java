package com.project.spent.controllers;

import com.project.spent.dtos.Credentials;
import com.project.spent.dtos.UserDTO;
import com.project.spent.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> add(@Valid @RequestBody final UserDTO dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") final Long id, @Valid @RequestBody final UserDTO dto) {
        service.update(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable("email") final String email) {
        return new ResponseEntity<>(service.getByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable("username") final String username) {
        return new ResponseEntity<>(service.getByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{id}/password/{password}")
    public ResponseEntity<UserDTO> getByPassword(@PathVariable("id") long id,
                                                 @PathVariable("password") String password) {
        return new ResponseEntity<>(service.getByPassword(id, password), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Credentials dto) {
        return new ResponseEntity<>(service.forgotPassword(dto), HttpStatus.OK);
    }

    @PutMapping("/reset/password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam("token") String confirmationToken, @RequestBody Credentials dto) {
        return new ResponseEntity<>(service.resetPassword(confirmationToken, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final Long id) {
        service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
