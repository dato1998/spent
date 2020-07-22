package com.project.spent.controllers;

import com.project.spent.dtos.CommentDTO;
import com.project.spent.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<CommentDTO> add(@Valid @RequestBody final CommentDTO dto) {
        System.out.println("add comment");
        return new ResponseEntity<>(service.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") final Long id, @Valid @RequestBody final CommentDTO dto) {
        service.update(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> get(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CommentDTO>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CommentDTO>> getAllByUserId(final Long userId) {
        return new ResponseEntity<>(service.getAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/post/{postId}", params = "page")
    public ResponseEntity<List<CommentDTO>> getAllByPostId(@PathVariable final Long postId,
                                                           @RequestParam("page") final int page) {
        return new ResponseEntity<>(service.getAllByPostId(postId, page), HttpStatus.OK);
    }

    @GetMapping("/parent")
    public ResponseEntity<List<CommentDTO>> getAllByParentId(final Long parentId) {
        return new ResponseEntity<>(service.getAllByParentId(parentId), HttpStatus.OK);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CommentDTO>> getRootComments() {
        return new ResponseEntity<>(service.getRootComments(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final Long id) {
        service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
