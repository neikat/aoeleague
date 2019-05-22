package com.sonic.aoeleague;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.sonic.aoeleague.controller.HomeController;

@SpringBootApplication
@ComponentScan(basePackageClasses=HomeController.class)
public class SpringBlogMvcApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBlogMvcApp.class, args);
    }
}
