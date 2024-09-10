package com.dashboard.backend.dto;

import java.util.List;

public class UserDto {
    public Long id;
    public String email;
    public String password;
    public String artistName;
//    public List<String> roleName;

    public UserDto(Long id, String email, String artistName) {
        this.id = id;
        this.email = email;
        this.artistName = artistName;
    }

    public UserDto(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
