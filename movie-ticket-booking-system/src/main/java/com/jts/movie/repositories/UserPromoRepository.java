package com.jts.movie.repositories;

import com.jts.movie.entities.UserPromo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPromoRepository extends JpaRepository<UserPromo, Long> {
    @Query("SELECT up FROM UserPromo up WHERE up.userToken = :userToken AND up.promo.title = :promoCode")
    UserPromo findByUserTokenAndPromoCode(@Param("userToken") String userToken, @Param("promoCode") String promoCode);
}


