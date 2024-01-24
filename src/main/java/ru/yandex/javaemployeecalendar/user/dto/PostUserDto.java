package ru.yandex.javaemployeecalendar.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostUserDto {
    @Email
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 256, message = ErrorConstants.TOO_LONG)
    private final String email;
    @NotBlank
    @Size(min = 8, message = ErrorConstants.TOO_SHORT)
    @Size(max = 16, message = ErrorConstants.TOO_LONG)
    private final String password;
    @NotNull
    private final int roleId;
}
