package com.project.spent.dtos;

import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import com.project.spent.enums.Topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Topic topic;
    private Boolean event;
    private Date startTime;
    private Date endTime;
    private Date postedAt;
    private UserDTO user;
    private List<FileDTO> files;

    public PostDTO() {
    }

    public PostDTO(Long id, String title, String description, String location, Topic topic, Boolean event,
                   Date startTime, Date endTime, Date postedAt, UserDTO user, List<FileDTO> files) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLocation(location);
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Boolean getEvent() {
        return event;
    }

    public void setEvent(Boolean event) {
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

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", topic=" + topic +
                ", event=" + event +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", postedAt=" + postedAt +
                ", user=" + user +
                ", files=" + files +
                '}';
    }
}
