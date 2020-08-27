package com.example.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EbuyApp {
    public static final String FILE_DIR = "/resources/static/images";

    public static void main(String[] args) {
        SpringApplication.run(EbuyApp.class, args);
    }

}
