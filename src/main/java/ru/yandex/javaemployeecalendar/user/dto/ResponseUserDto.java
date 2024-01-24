package ru.yandex.javaemployeecalendar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.user.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ResponseUserDto {
    private final int id;
    private final String email;
    private final String password;
    private final Role role;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
}
