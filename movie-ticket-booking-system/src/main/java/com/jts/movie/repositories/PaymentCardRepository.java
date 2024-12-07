package com.jts.movie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jts.movie.entities.PaymentCard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    List<PaymentCard> findByUserId(Long userId);
    Optional<PaymentCard> findByCardNumber(String cardNumber);
}

