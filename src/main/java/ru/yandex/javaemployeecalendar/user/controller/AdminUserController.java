package ru.yandex.javaemployeecalendar.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.javaemployeecalendar.user.dto.PatchUserDto;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;
import ru.yandex.javaemployeecalendar.user.service.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto postUser(@RequestBody @Valid PostUserDto postUserDto) {
        log.debug("Вызван метод postUser");
        return adminUserService.postUser(postUserDto);
    }

    @GetMapping("/{userId}")
    public ResponseUserDto getUserById(@PathVariable int userId) {
        log.debug("Вызван метод getUserById");
        return adminUserService.getUserById(userId);
    }

    @GetMapping("/time/{userId}")
    public int getUserWorkingTimeById(@PathVariable int userId) {
        log.debug("Вызван метод getUserWorkingTimeById");
        return adminUserService.getUserWorkingTimeByUserId(userId);
    }

    @GetMapping()
    public List<ResponseUserDto> getAllUsers(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.debug("Вызван метод getAllUsers");
        return adminUserService.getAllUsers(from, size);
    }

    @PatchMapping("/{userId}")
    public ResponseUserDto patchUser(@RequestBody @Valid PatchUserDto patchUserDto,
                                     @PathVariable int userId) {
        log.debug("Вызван метод patchUser");
        return adminUserService.patchUser(patchUserDto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        log.debug("Вызван метод deleteUser");
        adminUserService.deleteUser(userId);
    }

}
