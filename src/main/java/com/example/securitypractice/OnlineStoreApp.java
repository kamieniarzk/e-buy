package com.example.securitypractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineStoreApp {
    public static final String FILE_DIR = "/resources/static/images";

    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApp.class, args);
    }

}
