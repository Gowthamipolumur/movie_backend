package com.jts.movie.services;

import com.jts.movie.entities.Prices;
import com.jts.movie.repositories.PricesRepository;
import org.springframework.stereotype.Service;

@Service
public class PricesService {

    private final PricesRepository pricesRepository;

    public PricesService(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

    public Prices getPrices() {
        return pricesRepository.findById(1L).orElseThrow(() -> new RuntimeException("Prices not found"));
    }
}
