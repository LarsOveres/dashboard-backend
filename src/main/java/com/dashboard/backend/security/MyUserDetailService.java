package com.dashboard.backend.security;

import com.dashboard.backend.model.User;
import com.dashboard.backend.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepos;

    public MyUserDetailService(UserRepository repos) {
        this.userRepos = repos;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailService(this.userRepos);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }


}
