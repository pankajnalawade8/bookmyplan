package com.example.bookmypan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class bus {

    @GetMapping("/bus")
    public String getData() {
        return "Hello Welcome to BookMyPlan, Please book bus tickets at 10% discount";
    }
}