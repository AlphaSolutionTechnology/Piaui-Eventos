package com.alphasolutions.piauieventos;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class PiauieventosApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        SpringApplication.run(PiauieventosApplication.class, args);
    }

}
