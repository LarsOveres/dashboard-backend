package com.dashboard.backend.dto;

public class CommentDto {

    private Long id;
    private String content;
    private String artistName;
    private Long fileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return artistName;
    }

    public void setUserName(String artistName) {
        this.artistName = artistName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
