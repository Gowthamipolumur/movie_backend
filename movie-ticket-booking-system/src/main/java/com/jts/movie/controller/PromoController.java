package com.jts.movie.controller;

import com.jts.movie.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/promo")
public class PromoController {

    @Autowired
    private PromotionService promoService;

    @PostMapping("/checkPromo")
    public ResponseEntity<Map<String, Object>> checkPromo(@RequestBody Map<String, String> request) {
        String userToken = request.get("userToken");
        String promoCode = request.get("promoCode");
        Map<String, Object> response = promoService.checkPromo(userToken, promoCode);
        return ResponseEntity.ok(response);
    }
}

