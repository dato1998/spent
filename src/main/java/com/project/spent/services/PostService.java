package com.project.spent.services;

import com.project.elastic.dtos.SearchPostDTO;
import com.project.elastic.services.ElasticPostService;
import com.project.exceptions.EntityNotFoundException;
import com.project.spent.dtos.PostDTO;
import com.project.spent.models.Post;
import com.project.spent.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository repository;
    private final ElasticPostService elasticPostService;
    private final ModelMapper mapper;

    public PostService(PostRepository repository, ElasticPostService elasticPostService, ModelMapper mapper) {
        this.repository = repository;
        this.elasticPostService = elasticPostService;
        this.mapper = mapper;
    }

    @Transactional
    public PostDTO add(PostDTO dto) {
        Post post = repository.save(dtoToEntity(dto));
        elasticPostService.createIndexWithMapping();
        elasticPostService.save(post);
        return entityToDto(post);
    }

    @Transactional
    public void update(Long id, PostDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + id));
        Post post = dtoToEntity(dto);
        post.setId(id);
        elasticPostService.save(post);
        repository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDTO get(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + id));
        return entityToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAllByUserId(Long userId) {
        return repository.findAllByUserId(userId).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAll() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> search(SearchPostDTO dto) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> searchData = elasticPostService.searchPost(dto);
        List<Post> products = repository.findAllByIdIn((Set<String>) searchData.get("data"));
        map.put("data", products.stream().map(this::entityToDto).collect(Collectors.toList()));
        map.put("count", searchData.get("count"));
        return map;
    }

    @Transactional
    public void delete(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + id));
        elasticPostService.delete(post);
        repository.deleteById(id);
    }

    private PostDTO entityToDto(Post post) {
        return mapper.map(post, PostDTO.class);
    }

    private Post dtoToEntity(PostDTO dto) {
        return mapper.map(dto, Post.class);
    }
}
