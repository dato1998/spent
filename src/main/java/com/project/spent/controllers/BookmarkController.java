package com.project.spent.controllers;

import com.project.spent.dtos.PostDTO;
import com.project.spent.dtos.UserDTO;
import com.project.spent.services.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{postId}")
    public ResponseEntity add(@Valid @RequestBody final UserDTO dto, @PathVariable("postId") final Long postId) {
        service.add(dto, postId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity delete(@Valid @RequestBody final UserDTO dto, @PathVariable("postId") final Long postId) {
        service.delete(dto, postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}", params = "page")
    public ResponseEntity<List<PostDTO>> getByUserId(@PathVariable("userId") final Long userId, @RequestParam("page") int page) {
        return new ResponseEntity<>(service.getByUserId(userId, page), HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<List<UserDTO>> getByPostId(final Long postId) {
        return new ResponseEntity<>(service.getByPostId(postId), HttpStatus.OK);
    }
}
