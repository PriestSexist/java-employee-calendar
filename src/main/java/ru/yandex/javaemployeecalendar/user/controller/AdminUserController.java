package ru.yandex.javaemployeecalendar.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.javaemployeecalendar.user.dto.UserDto;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.service.AdminUserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody @Valid UserDto userDto) {
        log.debug("Вызван метод postUser");
        return adminUserService.postUser(userDto);
    }

}
