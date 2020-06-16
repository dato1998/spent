package com.project.files.dtos;

public class FileDTO {
    private Long id;
    private String name;
    private String path;
    private String contentType;
    private String extension;
    private String size;
    private String lastUpdateTime;
    private String lastUpdatedBy;

    public FileDTO() {
    }

    public FileDTO(Long id, String name, String path, String contentType, String extension, String size,
                   String lastUpdateTime, String lastUpdatedBy) {
        setId(id);
        setName(name);
        setPath(path);
        setContentType(contentType);
        setExtension(extension);
        setSize(size);
        setLastUpdateTime(lastUpdateTime);
        setLastUpdatedBy(lastUpdatedBy);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
