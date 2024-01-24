package ru.yandex.javaemployeecalendar.user.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.User;

@UtilityClass
public class UserMapper {
    public static User createNewUser(PostUserDto postUserDto, Role role) {
        return User.builder()
                .email(postUserDto.getEmail())
                .password(postUserDto.getPassword())
                .role(role)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }

    public static ResponseUserDto createResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .isAccountNonExpired(user.isAccountNonExpired())
                .isAccountNonLocked(user.isAccountNonLocked())
                .isCredentialsNonExpired(user.isCredentialsNonExpired())
                .isEnabled(user.isEnabled())
                .build();
    }
}
