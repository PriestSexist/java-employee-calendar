package ru.yandex.javaemployeecalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class JavaEmployeeCalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEmployeeCalendarApplication.class, args);
    }

}
