package com.jts.movie.repositories;

import com.jts.movie.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByTitle(String title);
}
