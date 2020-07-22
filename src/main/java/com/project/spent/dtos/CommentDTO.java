package com.project.spent.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import com.project.spent.models.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDTO {
    private Long id;
    private String text;
    private Boolean replies;
    @JsonFormat(timezone = "GMT+04:00")
    private Date commentedAt;
    private UserDTO user;
    private PostDTO post;
    private CommentDTO parent;
    private List<FileDTO> files;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, Boolean replies, Date commentedAt, UserDTO user,
                      PostDTO post, CommentDTO parent, List<FileDTO> files) {
        setId(id);
        setText(text);
        setReplies(replies);
        setCommentedAt(commentedAt);
        setUser(user);
        setPost(post);
        setParent(parent);
        setFiles(files);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getReplies() {
        return replies;
    }

    public void setReplies(Boolean replies) {
        this.replies = replies;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt == null ? new Date() : commentedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public CommentDTO getParent() {
        return parent;
    }

    public void setParent(CommentDTO parent) {
        this.parent = parent;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files == null ? new ArrayList<>() : files;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", replies=" + replies +
                ", commentedAt=" + commentedAt +
                ", user=" + user +
                ", post=" + post +
                ", parent=" + parent +
                ", files=" + files +
                '}';
    }
}
