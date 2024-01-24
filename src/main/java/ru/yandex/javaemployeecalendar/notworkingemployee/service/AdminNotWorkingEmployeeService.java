package ru.yandex.javaemployeecalendar.notworkingemployee.service;

import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;

import java.util.List;

public interface AdminNotWorkingEmployeeService {
    ResponseNotWorkingEmployeeDto postNotWorkingEmployee(PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto);

    ResponseNotWorkingEmployeeDto getCurrentNotWorkingEmployeeByUserId(int userId);

    List<ResponseNotWorkingEmployeeDto> getAllNotWorkingEmployee(int from, int size);

    ResponseNotWorkingEmployeeDto patchNotWorkingEmployee(PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto, int notWorkingEmployeeId);

    void deleteNotWorkingEmployee(int notWorkingEmployeeId);

}
