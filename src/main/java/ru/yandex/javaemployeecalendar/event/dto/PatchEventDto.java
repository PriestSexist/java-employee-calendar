package ru.yandex.javaemployeecalendar.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PatchEventDto {
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 256, message = ErrorConstants.TOO_LONG)
    private final String name;
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 2048, message = ErrorConstants.TOO_LONG)
    private final String description;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime startTime;
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime endTime;
    private final Integer responsibleId;
}
