package com.dashboard.backend.controller;

import com.dashboard.backend.dto.ProfileDto;
import com.dashboard.backend.model.Profile;
import com.dashboard.backend.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileRepository repos;
    public ProfileController(ProfileRepository repos) {
        this.repos = repos;
    }

    @PostMapping("/user")
    public ResponseEntity<Profile> createProfile(@RequestBody ProfileDto profileDto) {
        Profile profile = new Profile();
        profile.setArtistName(profileDto.artistName);
        profile.setFname(profileDto.fname);
        profile.setLname(profileDto.lname);
        profile.setEmail(profileDto.email);

        this.repos.save(profile);

        return ResponseEntity.created(null).body(profile);
    }

    @GetMapping("/{artistName}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String artistName, @AuthenticationPrincipal UserDetails userDetails) {

        if (artistName.equals(userDetails.getUsername())) {

            Profile profile = this.repos.findById(artistName).get();
            ProfileDto profileDto = new ProfileDto();
            profileDto.artistName = profile.getArtistName();
            profileDto.fname = profile.getFname();
            profileDto.lname = profile.getLname();
            profileDto.email = profile.getEmail();

            return ResponseEntity.ok(profileDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
