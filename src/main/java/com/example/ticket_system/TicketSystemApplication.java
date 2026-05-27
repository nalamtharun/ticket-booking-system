package com.example.ticket_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.ticketsystem.repository")
@EntityScan(basePackages = "com.example.ticketsystem.entity") // change to .models if your folder is named models
@ComponentScan(basePackages = "com.example.ticketsystem")
public class TicketSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }
}