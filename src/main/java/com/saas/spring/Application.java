package com.saas.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    @GetMapping("/")
    public String root(){
        return "Fan de los travas";
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}