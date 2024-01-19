package ru.yandex.javaemployeecalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


// TODO В рабочем календаре должны быть реализованы такие возможности: авторизация по паре почта-пароль,
//  возможность добавления рабочих событий на временные промежутки в течении дня,
//  возможность добавления отпусков и больничных по дням в течении месяца,
//  подсчёт рабочего времени сотрудника в течении месяца(т.е 8-часовой рабочий день в течении всех рабочих дней в месяце за исключением отпусков и больничных).
//  REST API должен предоставлять эндпоинты для CRUD операций по данному функционалу.
//  Данные получаемые по эндпоинтам должны иметь валидацию и различные статусы ответов в зависимости от ошибок.
//  Проект должен быть покрыт юнит-тестами.
//  Все данные должны сохранятся в базу данных.
//  Конечный результат должен быть выложен в открытый репозиторий на Github с актуальной историей коммитов и JAR файлом проекта.

// Стек технологий: Spring Boot 5+, JUnit 5, Mockito, Maven, Spring Data Jpa, PostgreSQL/MySQL.
// Будет плюсом:
// Описание эндпоинтов с помощью Swagger.
// Dockerfile конечной версии проекта.
@SpringBootApplication
@EnableMethodSecurity()
public class JavaEmployeeCalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEmployeeCalendarApplication.class, args);
    }

}
