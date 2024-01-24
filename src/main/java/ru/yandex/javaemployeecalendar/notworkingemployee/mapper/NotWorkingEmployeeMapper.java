package ru.yandex.javaemployeecalendar.notworkingemployee.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;
import ru.yandex.javaemployeecalendar.user.model.User;

@UtilityClass
public class NotWorkingEmployeeMapper {
    public static NotWorkingEmployee createNotWorkingEmployee(PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto, User employee) {
        return NotWorkingEmployee.builder()
                .employee(employee)
                .startTime(postPatchNotWorkingEmployeeDto.getStartTime())
                .endTime(postPatchNotWorkingEmployeeDto.getEndTime())
                .description(postPatchNotWorkingEmployeeDto.getDescription())
                .build();
    }

    public static ResponseNotWorkingEmployeeDto createResponseNotWorkingEmployee(NotWorkingEmployee notWorkingEmployee) {
        return ResponseNotWorkingEmployeeDto.builder()
                .id(notWorkingEmployee.getId())
                .employee(notWorkingEmployee.getEmployee())
                .startTime(notWorkingEmployee.getStartTime())
                .endTime(notWorkingEmployee.getEndTime())
                .description(notWorkingEmployee.getDescription())
                .build();
    }
}
