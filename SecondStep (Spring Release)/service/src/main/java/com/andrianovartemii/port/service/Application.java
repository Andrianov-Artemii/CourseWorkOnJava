package com.andrianovartemii.port.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication();
        springApplication.setDefaultProperties(Collections.singletonMap("localhost", "8082"));
        springApplication.run(com.andrianovartemii.port.service.Application.class, args);
    }
}
