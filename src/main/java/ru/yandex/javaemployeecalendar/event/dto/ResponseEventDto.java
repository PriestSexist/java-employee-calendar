package ru.yandex.javaemployeecalendar.event.dto;

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
public class ResponseEventDto {
    private final int id;
    private final String name;
    private final String description;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime startTime;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime endTime;
    private final User responsible;
}
