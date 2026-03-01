package com.saas.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Saas de aprendizaje federado",
        version = "1.0 Beta",
        description = "Api para gestionar y aprender, daryl come trava"
    )
)
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