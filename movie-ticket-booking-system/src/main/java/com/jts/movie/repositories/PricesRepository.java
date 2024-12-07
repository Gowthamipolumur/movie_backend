package com.jts.movie.repositories;

import com.jts.movie.entities.Prices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricesRepository extends JpaRepository<Prices, Long> {
    // Custom queries can be added if necessary
}

