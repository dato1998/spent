package com.project.spent.models;

import com.project.files.models.File;

import javax.persistence.*;
import java.util.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", nullable = false, length = 500)
    private String title;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "location")
    private String location;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "is_event", nullable = false)
    private boolean event;
    @Column(name = "startTime")
    private Date startTime;
    @Column(name = "endTime")
    private Date endTime;
    @Column(name = "postedAt", nullable = false)
    private Date postedAt;
    @Column(name = "comments")
    private Long commentsNumber;
    @Column(name = "subscribers")
    private Long subscribersNumber;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<File> files;
    @ManyToMany(mappedBy = "subscribedPosts", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<User> subscribedUsers;
    @ManyToMany(mappedBy = "bookmarkedPosts", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<User> bookmarkedUsers;

    public Post() {
    }

    public Post(String title, String description, String location, Double latitude, Double longitude, String topic,
                Boolean event, Date startTime, Date endTime, Date postedAt, User user, List<Comment> comments,
                List<File> files) {
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
        setComments(comments);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files == null ? new ArrayList<>() : files;
    }

    public Long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(Long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Long getSubscribersNumber() {
        return subscribersNumber;
    }

    public void setSubscribersNumber(Long subscribersNumber) {
        this.subscribersNumber = subscribersNumber;
    }

    public Set<User> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<User> subscribedUsers) {
        this.subscribedUsers = subscribedUsers == null ? new HashSet<>() : subscribedUsers;
    }

    public Set<User> getBookmarkedUsers() {
        return bookmarkedUsers;
    }

    public void setBookmarkedUsers(Set<User> bookmarkedUsers) {
        this.bookmarkedUsers = bookmarkedUsers == null ? new HashSet<>() : bookmarkedUsers;
    }
}
