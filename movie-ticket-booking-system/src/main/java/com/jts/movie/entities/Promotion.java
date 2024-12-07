package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;



@Entity
@Table(name = "PROMOTIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long promoId;

    @Column(name = "description")
    private String description;

    @Column(name = "send_date")
    private Date sendDate;

    @Column(name = "sent", nullable = false)
    private boolean sent = false;


    @Column(name = "title")
    private String title;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;


    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "promo_name", nullable = false)
    private String promoName;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    // Method to update validity based on the current date
    public void updateValidity() {
        Date currentDate = new Date();
        if (startDate != null && endDate != null) {
            this.isValid = currentDate.after(startDate) && currentDate.before(endDate);
        } else {
            this.isValid = false;
        }
    }
}
