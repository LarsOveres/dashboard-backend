package com.dashboard.backend.repository;

import com.dashboard.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findFirstByEmail(String email);

//    Optional<User> findById(Long id);

    boolean existsByEmail(String email);
}


