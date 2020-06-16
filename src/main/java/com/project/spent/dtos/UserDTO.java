package com.project.spent.dtos;

import com.project.files.dtos.FileDTO;
import com.project.files.models.File;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String description;
    private FileDTO photo;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String password, String fullName, String description, FileDTO photo) {
        setId(id);
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setFullName(fullName);
        setDescription(description);
        setPhoto(photo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FileDTO getPhoto() {
        return photo;
    }

    public void setPhoto(FileDTO photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + photo +
                '}';
    }
}
