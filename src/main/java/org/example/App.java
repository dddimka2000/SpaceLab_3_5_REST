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
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.example.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}