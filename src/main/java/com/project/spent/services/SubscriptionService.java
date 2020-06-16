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
public class SubscriptionService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public SubscriptionService(PostRepository postRepository, UserRepository userRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void add(UserDTO userDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id :" + postId));
        post.getSubscribedUsers().add(userDtoToEntity(userDTO));
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id :" + userDTO.getId()));
        user.getSubscribedPosts().add(post);
        userRepository.save(user);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id : " + userId));
        return user.getSubscribedPosts()
                .stream()
                .map(this::postEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + postId));
        return post.getSubscribedUsers()
                .stream()
                .map(this::userEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UserDTO userDTO, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post was not found for given id : " + postId));
        post.getSubscribedUsers().remove(userDtoToEntity(userDTO));
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("user was not found for given id :" + userDTO.getId()));
        user.getSubscribedPosts().remove(post);
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
