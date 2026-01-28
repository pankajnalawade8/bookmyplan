package com.example.bookmypan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RailwaysController {

    @GetMapping("/railways") // This is the path!
    public String getData() {
        return "Hello Welcome to BookMyPlan...";
    }
}
