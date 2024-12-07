package com.jts.movie.services;

import com.jts.movie.entities.Theater;
import com.jts.movie.repositories.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public Theater addTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(Long theaterId) throws Exception {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new Exception("Theater not found with ID: " + theaterId));
    }

    public Theater updateTheater(Long theaterId, Theater updatedTheater) throws Exception {
        Theater theater = getTheaterById(theaterId);
        theater.setName(updatedTheater.getName());
        theater.setLocation(updatedTheater.getLocation());
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long theaterId) throws Exception {
        Theater theater = getTheaterById(theaterId);
        theaterRepository.delete(theater);
    }
}
