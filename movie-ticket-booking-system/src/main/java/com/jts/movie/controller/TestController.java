package com.jts.movie.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
public class TestController {

    @GetMapping("/status")
    public String test(){
        return "OK, I'm up and running!!";
    }
}
