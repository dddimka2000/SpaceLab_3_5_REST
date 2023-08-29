package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class App extends SpringBootServletInitializer{
    public static void main(String[] args) throws Throwable{
        SpringApplication.run(App.class, args);


    }

}