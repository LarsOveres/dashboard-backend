package com.dashboard.backend.security;

import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepos;

    public MyUserDetailService(UserRepository repos) {
        this.userRepos = repos;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailService(this.userRepos); // Zorg ervoor dat MyUserDetailService zoekt op e-mail
    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<User> ou = userRepos.findByEmail(email);
//        if (ou.isPresent()) {
//            User user = (User) ou.get();
//            return new com.dashboard.backend.security.MyUserDetails(user);
//        } else {
//            throw new UsernameNotFoundException(email);
//        }
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Veronderstel dat 'username' hier een gebruikers-ID is
//        Long userId;
//        try {
//            userId = Long.parseLong(username);
//        } catch (NumberFormatException e) {
//            throw new UsernameNotFoundException("Invalid user ID format: " + username);
//        }
//
//        User user = userRepos.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepos.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
//    }

//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Long id;
//        try {
//            id = Long.parseLong(userId); // Zet de userId om naar een Long
//        } catch (NumberFormatException e) {
//            throw new UsernameNotFoundException("Invalid user ID format");
//        }
//
//        User user = userRepos.findByEmail(id)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Zoek de gebruiker op basis van het e-mailadres
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }


}
