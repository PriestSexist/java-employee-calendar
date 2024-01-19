package ru.yandex.javaemployeecalendar.user.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.javaemployeecalendar.user.dto.UserDto;
import ru.yandex.javaemployeecalendar.user.model.User;

public interface AdminUserService {
    User postUser(UserDto userDto);
}
