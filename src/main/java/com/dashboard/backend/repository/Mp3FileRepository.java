package com.dashboard.backend.repository;

import com.dashboard.backend.model.Mp3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Mp3FileRepository extends JpaRepository<Mp3File, Long> {
}
