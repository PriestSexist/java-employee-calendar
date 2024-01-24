package ru.yandex.javaemployeecalendar.notworkingemployee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;
import ru.yandex.javaemployeecalendar.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ResponseNotWorkingEmployeeDto {
    private final int id;
    private final User employee;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private LocalDateTime startTime;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private LocalDateTime endTime;
    private final String description;
}
