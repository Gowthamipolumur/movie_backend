package com.jts.movie.convertor;

import com.jts.movie.entities.Show;
import com.jts.movie.request.ShowRequest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowConvertor {

    public static Show showDtoToShow(ShowRequest showRequest) {
        // Convert java.util.Date to java.sql.Date for the date field
        Date showDate = Date.valueOf(showRequest.getShowDate().toLocalDate());

        // Convert java.util.Time to java.sql.Time for the time field
        Time showStartTime = Time.valueOf(showRequest.getShowStartTime().toLocalTime());

        LocalDate localDate = showDate.toLocalDate();
        LocalTime localTime = showStartTime.toLocalTime();

        // Use the builder to create a Show object
        Show show = Show.builder()
                .date(localDate)         // Set the date field
                .time(localTime)    // Set the time field
                .build();

        return show;
    }
}
