package com.jts.movie.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jts.movie.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailId(String emailId);;
    Optional<User> findByConfirmationToken(String token);
    Optional<User> findByResetToken(String resetToken);
    @Query("SELECT u FROM User u WHERE u.promotionPreference = true AND u.isActive = true")
    List<User> findByPromotionPreferenceTrue();
    List<User> findByIsActive(boolean isActive);
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.promotionPreference = true")
    List<User> findByIsActiveAndPromotionPreference();

}