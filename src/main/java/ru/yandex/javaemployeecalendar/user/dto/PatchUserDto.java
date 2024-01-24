package ru.yandex.javaemployeecalendar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PatchUserDto {
    @Email
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 256, message = ErrorConstants.TOO_LONG)
    private final String email;
    @Size(min = 8, message = ErrorConstants.TOO_SHORT)
    @Size(max = 16, message = ErrorConstants.TOO_LONG)
    private final String password;
    @NotNull
    private final Integer roleId;
    private final Boolean isAccountNonExpired;
    private final Boolean isAccountNonLocked;
    private final Boolean isCredentialsNonExpired;
    private final Boolean isEnabled;
}
