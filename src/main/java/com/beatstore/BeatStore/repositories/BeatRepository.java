package com.beatstore.BeatStore.repositories;


import com.beatstore.BeatStore.models.Beat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeatRepository extends JpaRepository<Beat, Long> {
    Optional<Beat> findByTitle(String title);

    boolean existsByTitle(String title);
}
