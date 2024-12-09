package com.jts.movie.repositories;

import com.jts.movie.entities.UserPromo;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromoRepository extends JpaRepository<UserPromo, Long> {
    @Query("SELECT up FROM UserPromo up WHERE up.userToken = :userToken AND up.promoName = :promoName")
    Optional<UserPromo> findByUserTokenAndPromoName(@Param("promoName") String promoName,@Param("userToken") String userToken);


    // Change the return type to Optional<UserPromo>
    Optional<UserPromo> findByUserToken(@Email(message = "Email is invalid") String emailId);

    List<UserPromo> findByPromoName(String promoName);
}
