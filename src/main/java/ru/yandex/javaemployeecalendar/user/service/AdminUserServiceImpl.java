package ru.yandex.javaemployeecalendar.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.javaemployeecalendar.user.dto.UserDto;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User postUser(UserDto userDto) {
        User user = new User(0, userDto.getEmail(), userDto.getPassword(), Set.copyOf(userDto.getRoles().stream().map(string -> new Role(1, string)).collect(Collectors.toList())));
        return userRepository.save(user);
    }
}
