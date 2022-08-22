package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class Course3SchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(Course3SchoolApplication.class, args);
    }

}
