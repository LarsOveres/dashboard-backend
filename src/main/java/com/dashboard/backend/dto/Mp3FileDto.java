package com.dashboard.backend.dto;

import com.dashboard.backend.model.User;

public class Mp3FileDto {
    private Long id;

    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private User user;
    private String artistName;
    private Long duration;
    private int playCount;
    private int downloadCount;

    public Mp3FileDto(Long id, String fileName, String filePath, User user, String artistName, Long fileSize, String fileType, int playCount, int downloadCount) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.user = user;
        this.artistName = artistName;
        this.id = id;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.playCount = playCount;
        this.downloadCount = downloadCount;
    }

    public Mp3FileDto() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}
