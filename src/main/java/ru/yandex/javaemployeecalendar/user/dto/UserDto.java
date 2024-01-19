package ru.yandex.javaemployeecalendar.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDto {
    private final int id;
    @Email
    private final String email;
    private final String password;
    private final List<String> roles;
}
