package com.alphasolutions.piauieventos;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.Objects;

@SpringBootApplication
public class PiauieventosApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        System.setProperty("API_KEY", Objects.requireNonNull(dotenv.get("API_KEY")));
        SpringApplication.run(PiauieventosApplication.class, args);
    }

}
