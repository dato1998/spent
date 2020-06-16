package com.project.spent.controllers;

import com.project.spent.dtos.PostDTO;
import com.project.spent.dtos.UserDTO;
import com.project.spent.models.Post;
import com.project.spent.services.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService service;

    public BookmarkController(BookmarkService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity add(@Valid @RequestBody final UserDTO dto, final Long postId) {
        service.add(dto, postId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity delete(@Valid @RequestBody final UserDTO dto, final Long postId) {
        service.delete(dto, postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getByUserId(final Long userId) {
        return new ResponseEntity<>(service.getByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<List<UserDTO>> getByPostId(final Long postId) {
        return new ResponseEntity<>(service.getByPostId(postId), HttpStatus.OK);
    }
}
