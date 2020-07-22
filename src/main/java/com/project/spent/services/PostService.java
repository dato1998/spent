package com.project.spent.services;

import com.project.elastic.dtos.SearchPostDTO;
import com.project.elastic.services.ElasticPostService;
import com.project.exceptions.EntityNotFoundException;
import com.project.files.dtos.FileDTO;
import com.project.files.services.CloudinaryStorageService;
import com.project.spent.dtos.PostDTO;
import com.project.spent.models.Post;
import com.project.spent.repositories.CommentRepository;
import com.project.spent.repositories.PostRepository;
import com.project.spent.services.util.FileHelper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository repository;
    private final CommentRepository commentRepository;
    private final CloudinaryStorageService cloudinaryStorageService;
    private final ElasticPostService elasticPostService;
    private final ModelMapper mapper;

    public PostService(PostRepository repository, CommentRepository commentRepository,
                       CloudinaryStorageService cloudinaryStorageService,
                       ElasticPostService elasticPostService, ModelMapper mapper) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.cloudinaryStorageService = cloudinaryStorageService;
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
        Post oldPost = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + id));
        Post post = dtoToEntity(dto);
        post.setId(id);
        post.setSubscribedUsers(oldPost.getSubscribedUsers());
        post.setBookmarkedUsers(oldPost.getBookmarkedUsers());
        elasticPostService.save(post);
        repository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDTO get(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + id));

        final Long comments = commentRepository.countByPostId(post.getId());
        final PostDTO postDTO = entityToDto(post);
        postDTO.setCommentsNumber(comments);
        final Optional<String> fileObject = FileHelper.getPhotoAsString(cloudinaryStorageService, postDTO.getUser());
        fileObject.ifPresent(s -> postDTO.getUser().getPhoto().setFileObject(s));

        return postDTO;
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAllEventsByUserId(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 2);
        final List<Post> posts = new ArrayList<>(repository.findAllByUserIdAndEventTrueOrderByPostedAtDesc(userId, pageable));
        return getAllFieldsForPost(posts);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAllRegularPostsByUserId(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 2);
        final List<Post> posts = new ArrayList<>(repository.findAllByUserIdAndEventFalseOrderByPostedAtDesc(userId, pageable));
        return getAllFieldsForPost(posts);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAll(final int page) {
        Pageable pageable = PageRequest.of(page, 2);
        final List<Post> posts = new ArrayList<>(repository.findAllByOrderByPostedAtDesc(pageable));
        return getAllFieldsForPost(posts);
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

    private List<PostDTO> getAllFieldsForPost(List<Post> posts) {
        final List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            final Long comments = commentRepository.countByPostId(post.getId());
            final PostDTO postDTO = entityToDto(post);
            postDTO.setCommentsNumber(comments);

            final FileDTO fileDTO = postDTO.getUser().getPhoto();
            if (fileDTO != null) {
                final byte[] photoAsBytes = cloudinaryStorageService.getFile(fileDTO.getPath());
                if (photoAsBytes != null) {
                    final String photoAsString = Base64.getEncoder().encodeToString(photoAsBytes);
                    fileDTO.setFileObject(photoAsString);
                    postDTO.getUser().setPhoto(fileDTO);
                }
            }

            postDTOS.add(postDTO);
        }

        return postDTOS;
    }
}
