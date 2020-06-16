package com.project.elastic.model;

import com.project.spent.enums.Topic;

import java.util.Date;

public class ElasticPost {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Topic topic;
    private Boolean event;
    private Long userId;
    private String username;
    private Date startTime;
    private Date endTime;
    private Date createdAt;

    public ElasticPost() {
    }

    public ElasticPost(Long id, String title, String description, String location, Topic topic, Boolean event,
                       Long userId, String username, Date startTime, Date endTime, Date createdAt) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setTopic(topic);
        setEvent(event);
        setUserId(userId);
        setUsername(username);
        setStartTime(startTime);
        setEndTime(endTime);
        setCreatedAt(createdAt);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt == null ? new Date() : createdAt;
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private String location;
        private Topic topic;
        private Boolean event;
        private Long userId;
        private String username;
        private Date startTime;
        private Date endTime;
        private Date createdAt;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setTopic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public Builder setEvent(Boolean event) {
            this.event = event;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setStartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt == null ? new Date() : createdAt;
            return this;
        }

        public ElasticPost build() {
            ElasticPost elasticPost = new ElasticPost();
            elasticPost.setId(id);
            elasticPost.setTitle(title);
            elasticPost.setDescription(description);
            elasticPost.setLocation(location);
            elasticPost.setTopic(topic);
            elasticPost.setEvent(event);
            elasticPost.setUserId(userId);
            elasticPost.setUsername(username);
            elasticPost.setStartTime(startTime);
            elasticPost.setEndTime(endTime);
            elasticPost.setCreatedAt(createdAt);
            return elasticPost;
        }
    }
}
