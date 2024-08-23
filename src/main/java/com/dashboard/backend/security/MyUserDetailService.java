package com.dashboard.backend.security;

import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepos;

    public MyUserDetailService(UserRepository repos) {
        this.userRepos = repos;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> ou = userRepos.findByEmail(email);
        if (ou.isPresent()) {
            User user = (User) ou.get();
            return new com.dashboard.backend.security.MyUserDetails(user);
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
