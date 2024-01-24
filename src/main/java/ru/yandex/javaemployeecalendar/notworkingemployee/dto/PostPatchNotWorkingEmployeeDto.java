package ru.yandex.javaemployeecalendar.notworkingemployee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.javaemployeecalendar.error.constants.ErrorConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostPatchNotWorkingEmployeeDto {
    @NotNull
    private final Integer employeeId;
    @NotNull
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private LocalDateTime startTime;
    @NotNull
    @JsonFormat(pattern = ErrorConstants.DATETIME_FORMAT)
    private LocalDateTime endTime;
    @Size(min = 16, message = ErrorConstants.TOO_SHORT)
    @Size(max = 512, message = ErrorConstants.TOO_LONG)
    private final String description;
}
