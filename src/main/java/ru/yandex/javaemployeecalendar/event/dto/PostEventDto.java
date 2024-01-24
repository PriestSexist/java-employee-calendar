package ru.yandex.javaemployeecalendar.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostEventDto {
    @NotBlank
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 256, message = ErrorConstants.TOO_LONG)
    private final String name;
    @NotBlank
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 2048, message = ErrorConstants.TOO_LONG)
    private final String description;
    @NotNull
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime startTime;
    @NotNull
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private final LocalDateTime endTime;
    @NotNull
    private final Integer responsibleId;
}
