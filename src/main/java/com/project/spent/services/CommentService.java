package com.project.spent.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.spent.dtos.CommentDTO;
import com.project.spent.models.Comment;
import com.project.spent.repositories.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository repository;
    private final ModelMapper mapper;

    public CommentService(CommentRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CommentDTO add(CommentDTO dto) {
        Comment comment = repository.save(dtoToEntity(dto));
        if (dto.getParent() != null && !dto.getParent().getReplies()) {
            dto.getParent().setReplies(true);
            repository.save(dtoToEntity(dto.getParent()));
        }
        return entityToDto(comment);
    }

    @Transactional
    public void update(Long id, CommentDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("comment was not found for given id : " + id));
        Comment comment = dtoToEntity(dto);
        comment.setId(id);
        repository.save(comment);
    }

    @Transactional(readOnly = true)
    public CommentDTO get(Long id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("comment was not found for given id : " + id));
        return entityToDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByUserId(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByPostId(Long postId) {
        return repository.findAllByPostId(postId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByParentId(Long parentId) {
        return repository.findAllByParentId(parentId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getRootComments() {
        return repository.findAllByParentIsNull()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAll() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("comment was not found for given id : " + id));
        long replies = repository.countByParentId(comment.getParent().getId());
        if (comment.getParent() != null && replies == 1) {
            comment.getParent().setReplies(false);
            repository.save(comment.getParent());
        }
        repository.deleteById(id);
    }

    private CommentDTO entityToDto(Comment comment) {
        return mapper.map(comment, CommentDTO.class);
    }

    private Comment dtoToEntity(CommentDTO dto) {
        return mapper.map(dto, Comment.class);
    }
}
