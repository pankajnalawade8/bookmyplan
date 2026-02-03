package com.example.bookmypan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Railway {

    @GetMapping("/Railway")
    public String getData() {
        return "Hello Welcome to BookMyPlan, Please book railway tickets at 40% discount";
    }
}