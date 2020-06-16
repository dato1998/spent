package com.project.spent.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.spent.dtos.PostDTO;
import com.project.spent.dtos.UserDTO;
import com.project.spent.models.Post;
import com.project.spent.models.User;
import com.project.spent.repositories.PostRepository;
import com.project.spent.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public BookmarkService(UserRepository userRepository, PostRepository postRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void add(UserDTO userDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id :" + postId));
        post.getBookmarkedUsers().add(userDtoToEntity(userDTO));
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id :" + userDTO.getId()));
        user.getBookmarkedPosts().add(post);
        userRepository.save(user);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id : " + userId));
        return user.getBookmarkedPosts()
                .stream()
                .map(this::postEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + postId));
        return post.getBookmarkedUsers()
                .stream()
                .map(this::userEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UserDTO userDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + postId));
        post.getBookmarkedUsers().remove(userDtoToEntity(userDTO));
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id :" + userDTO.getId()));
        user.getBookmarkedPosts().remove(post);
        userRepository.save(user);
        postRepository.save(post);
    }

    private UserDTO userEntityToDto(User user) {
        return mapper.map(user, UserDTO.class);
    }

    private User userDtoToEntity(UserDTO dto) {
        return mapper.map(dto, User.class);
    }

    private PostDTO postEntityToDto(Post post) {
        return mapper.map(post, PostDTO.class);
    }
}
