package com.arthurlamberti.admin.catalogo.infrastructure;

import com.arthurlamberti.admin.catalogo.application.UseCase;
import com.arthurlamberti.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }

}