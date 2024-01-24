package ru.yandex.javaemployeecalendar.user.service;

import ru.yandex.javaemployeecalendar.user.dto.PatchUserDto;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;

import java.util.List;

public interface AdminUserService {
    ResponseUserDto postUser(PostUserDto postUserDto);

    ResponseUserDto getUserById(int userId);

    ResponseUserDto patchUser(PatchUserDto patchUserDto, int userId);

    void deleteUser(int userId);

    List<ResponseUserDto> getAllUsers(int from, int size);

    int getUserWorkingTimeByUserId(int userId);
}
