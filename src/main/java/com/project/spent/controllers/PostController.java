package com.project.spent.controllers;

import com.project.elastic.dtos.SearchPostDTO;
import com.project.spent.dtos.PostDTO;
import com.project.spent.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<PostDTO> add(@Valid @RequestBody final PostDTO dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") final Long id, @Valid @RequestBody final PostDTO dto) {
        service.update(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/", params = "page")
    public ResponseEntity<List<PostDTO>> getAll(@RequestParam("page") int page) {
        return new ResponseEntity<>(service.getAll(page), HttpStatus.OK);
    }

    @GetMapping(value = "/event/user/{userId}", params = "page")
    public ResponseEntity<List<PostDTO>> getAllEventsByUserId(@PathVariable("userId") final Long userId,
                                                              @RequestParam("page") int page) {
        return new ResponseEntity<>(service.getAllEventsByUserId(userId, page), HttpStatus.OK);
    }

    @GetMapping(value = "/regular/user/{userId}", params = "page")
    public ResponseEntity<List<PostDTO>> getAllRegularPostsByUserId(@PathVariable("userId") final Long userId,
                                                                    @RequestParam("page") int page) {
        return new ResponseEntity<>(service.getAllRegularPostsByUserId(userId, page), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody SearchPostDTO dto) {
        return new ResponseEntity<>(service.search(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final Long id) {
        service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
