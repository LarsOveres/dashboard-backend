//package com.dashboard.backend.service;
//
//import com.dashboard.backend.dto.ProfileDto;
//import com.dashboard.backend.model.Profile;
//import com.dashboard.backend.repository.ProfileRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class ProfileService {
//
//    private final ProfileRepository repos;
//
//    @Autowired
//    public ProfileService(ProfileRepository repos) {
//        this.repos = repos;
//    }
//
//    public Profile createProfile(ProfileDto profileDto) {
//        Profile profile = new Profile();
//        profile.setArtistName(profileDto.artistName);
//        profile.setFname(profileDto.fname);
//        profile.setLname(profileDto.lname);
//        profile.setEmail(profileDto.email);
//
//        return repos.save(profile);
//    }
//
//    public Optional<ProfileDto> getProfile(String ArtistName) {
//        return repos.findById(ArtistName).map(profile -> {
//            ProfileDto profileDto = new ProfileDto();
//            profileDto.artistName = profile.getArtistName();
//            profileDto.fname = profile.getFname();
//            profileDto.lname = profile.getLname();
//            profileDto.email = profile.getEmail();
//            return profileDto;
//        });
//    }
//}
