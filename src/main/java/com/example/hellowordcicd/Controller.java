package com.example.hellowordcicd;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String hello() {
        return "Autoamtyczne testowanie build o godzienie " + System.currentTimeMillis();
    }
}
