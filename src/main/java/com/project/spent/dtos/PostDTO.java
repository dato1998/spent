package com.project.spent.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.files.dtos.FileDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PostDTO {
    private Long id;
    private boolean event;
    private String title;
    private String description;
    @JsonFormat(timezone = "GMT+04:00")
    private Date startTime;
    @JsonFormat(timezone = "GMT+04:00")
    private Date endTime;
    private String location;
    private Double latitude;
    private Double longitude;
    private String topic;
    private UserDTO user;
    @JsonFormat(timezone = "GMT+04:00")
    private Date postedAt;
    private Long commentsNumber;
    private Set<UserDTO> subscribedUsers;
    private List<FileDTO> files;

    public PostDTO() {
    }

    public PostDTO(Long id, String title, String description, String location, Double latitude, Double longitude,
                   String topic, boolean event, Date startTime, Date endTime, Date postedAt, UserDTO user, List<FileDTO> files) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setLatitude(latitude);
        setLongitude(longitude);
        setTopic(topic);
        setEvent(event);
        setStartTime(startTime);
        setEndTime(endTime);
        setPostedAt(postedAt);
        setUser(user);
        setFiles(files);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt == null ? new Date() : postedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files == null ? new ArrayList<>() : files;
    }

    public Long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(Long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Set<UserDTO> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<UserDTO> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", event=" + event +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", topic='" + topic + '\'' +
                ", user=" + user +
                ", postedAt=" + postedAt +
                ", commentsNumber=" + commentsNumber +
                ", subscribedUsers=" + subscribedUsers +
                ", files=" + files +
                '}';
    }
}
