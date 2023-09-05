package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "MyProject",
                version = "1.0.0",
                description = "This is my project"
        )
)
public class App extends SpringBootServletInitializer{
    public static void main(String[] args) throws Throwable{
        SpringApplication.run(App.class, args);
    }
}