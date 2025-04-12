package com.bookmanagmentapp.bookmanagmentapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication // основная аннотация Spring Boot
@EnableAsync
public class BookManagmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookManagmentApplication.class, args);
    }
}
