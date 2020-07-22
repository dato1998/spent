package com.project.spent.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.files.services.CloudinaryStorageService;
import com.project.spent.dtos.CommentDTO;
import com.project.spent.dtos.UserDTO;
import com.project.spent.models.Comment;
import com.project.spent.repositories.CommentRepository;
import com.project.spent.services.util.FileHelper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository repository;
    private CloudinaryStorageService cloudinaryStorageService;
    private final ModelMapper mapper;

    public CommentService(CommentRepository repository, CloudinaryStorageService cloudinaryStorageService, ModelMapper mapper) {
        this.repository = repository;
        this.cloudinaryStorageService = cloudinaryStorageService;
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

        final CommentDTO commentDTO = entityToDto(comment);
        final UserDTO userDTO = commentDTO.getUser();
        if (userDTO.getPhoto() != null) {
            final Optional<String> fileObject = FileHelper.getPhotoAsString(cloudinaryStorageService, userDTO);
            fileObject.ifPresent(s -> userDTO.getPhoto().setFileObject(s));
        }

        return commentDTO;
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByUserId(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByPostId(Long postId, int page) {
        Pageable pageable = PageRequest.of(page, 2);
        List<CommentDTO> commentDTOS = repository.findAllByPostIdOrderByCommentedAtDesc(postId, pageable)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        for (CommentDTO commentDTO : commentDTOS) {
            final UserDTO userDTO = commentDTO.getUser();
            if (userDTO.getPhoto() != null) {
                final Optional<String> fileObject = FileHelper.getPhotoAsString(cloudinaryStorageService, userDTO);
                fileObject.ifPresent(s -> userDTO.getPhoto().setFileObject(s));
            }
        }

        return commentDTOS;
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
