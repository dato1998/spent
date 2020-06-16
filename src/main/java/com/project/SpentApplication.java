package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.project.spent.repositories", "com.project.security.session.repositories", "com.project.files.repositories"})
public class SpentApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpentApplication.class, args);
    }
}
