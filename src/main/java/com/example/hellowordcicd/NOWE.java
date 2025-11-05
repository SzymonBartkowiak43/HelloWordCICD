package com.example.hellowordcicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NOWE {


    @GetMapping("/nowe")
    public String nowe() {
        return "NOWE";
    }
}
