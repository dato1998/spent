package com.project.elastic.dtos;

import com.project.spent.enums.Topic;

import java.util.Date;

public class SearchPostDTO {
    private Long id;
    private String name;
    private String location;
    private Topic topic;
    private Boolean event;
    private Long userId;
    private String username;
    private Date startTimeFrom;
    private Date startTimeTo;
    private Date endTimeFrom;
    private Date endTimeTo;
    private Date createdAtFrom;
    private Date createdAtTo;
    private Integer limit;
    private Integer offset;

    public SearchPostDTO() {
    }

    public SearchPostDTO(Long id, String name, String location, Topic topic, Boolean event, Long userId, String username,
                         Date startTimeFrom, Date startTimeTo, Date endTimeFrom, Date endTimeTo, Date createdAtFrom,
                         Date createdAtTo, Integer limit, Integer offset) {
        setId(id);
        setName(name);
        setLocation(location);
        setTopic(topic);
        setEvent(event);
        setUserId(userId);
        setUsername(username);
        setStartTimeFrom(startTimeFrom);
        setStartTimeTo(startTimeTo);
        setEndTimeFrom(endTimeFrom);
        setEndTimeTo(endTimeTo);
        setCreatedAtFrom(createdAtFrom);
        setCreatedAtTo(createdAtTo);
        setLimit(limit);
        setOffset(offset);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getStartTimeFrom() {
        return startTimeFrom;
    }

    public void setStartTimeFrom(Date startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }

    public Date getStartTimeTo() {
        return startTimeTo;
    }

    public void setStartTimeTo(Date startTimeTo) {
        this.startTimeTo = startTimeTo;
    }

    public Date getEndTimeFrom() {
        return endTimeFrom;
    }

    public void setEndTimeFrom(Date endTimeFrom) {
        this.endTimeFrom = endTimeFrom;
    }

    public Date getEndTimeTo() {
        return endTimeTo;
    }

    public void setEndTimeTo(Date endTimeTo) {
        this.endTimeTo = endTimeTo;
    }

    public Date getCreatedAtFrom() {
        return createdAtFrom;
    }

    public void setCreatedAtFrom(Date createdAtFrom) {
        this.createdAtFrom = createdAtFrom;
    }

    public Date getCreatedAtTo() {
        return createdAtTo;
    }

    public void setCreatedAtTo(Date createdAtTo) {
        this.createdAtTo = createdAtTo;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "SearchProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", topic=" + topic +
                ", event=" + event +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", startTimeFrom=" + startTimeFrom +
                ", startTimeTo=" + startTimeTo +
                ", endTimeFrom=" + endTimeFrom +
                ", endTimeTo=" + endTimeTo +
                ", createdAtFrom=" + createdAtFrom +
                ", createdAtTo=" + createdAtTo +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
