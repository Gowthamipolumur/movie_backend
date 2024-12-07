package com.jts.movie.controller;


import com.jts.movie.entities.Prices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jts.movie.services.PricesService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PricesService pricesService;

    @GetMapping
    public ResponseEntity<Prices> getPrices() {
        Prices prices = pricesService.getPrices();
        return ResponseEntity.ok(prices);
    }
}

