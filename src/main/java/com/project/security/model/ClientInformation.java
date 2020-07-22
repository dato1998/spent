package com.project.security.model;

public class ClientInformation {
    private String username;
    private String fullName;
    private String email;
    private Long userId;
    private String token;
    private String fileObject;

    public ClientInformation() {
    }

    public ClientInformation(String username, String fullName, String email, Long userId, String token, String fileObject) {
        setUsername(username);
        setFullName(fullName);
        setEmail(email);
        setUserId(userId);
        setToken(token);
        setFileObject(fileObject);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileObject() {
        return fileObject;
    }

    public void setFileObject(String fileObject) {
        this.fileObject = fileObject;
    }
}
