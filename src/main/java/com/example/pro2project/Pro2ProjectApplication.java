package com.example.pro2project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.pro2project.repositories")
@EntityScan(basePackages = "com.example.pro2project.models")
@SpringBootApplication
public class Pro2ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Pro2ProjectApplication.class, args);
    }

}
