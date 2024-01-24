package ru.yandex.javaemployeecalendar.notworkingemployee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.service.AdminNotWorkingEmployeeService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/not-working")
@Validated
public class AdminNotWorkingEmployeeController {

    private final AdminNotWorkingEmployeeService adminNotWorkingEmployeeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseNotWorkingEmployeeDto postNotWorkingEmployee(@RequestBody @Valid PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto) {
        log.debug("Вызван метод postNotWorkingEmployee");
        return adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto);
    }

    @GetMapping("/{userId}")
    public ResponseNotWorkingEmployeeDto getCurrentNotWorkingEmployeeByUserId(@PathVariable int userId) {
        log.debug("Вызван метод getCurrentNotWorkingEmployeeByUserId");
        return adminNotWorkingEmployeeService.getCurrentNotWorkingEmployeeByUserId(userId);
    }

    @GetMapping()
    public List<ResponseNotWorkingEmployeeDto> getAllNotWorkingEmployee(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        log.debug("Вызван метод getAllNotWorkingEmployee");
        return adminNotWorkingEmployeeService.getAllNotWorkingEmployee(from, size);
    }

    @PatchMapping("/{notWorkingEmployeeId}")
    public ResponseNotWorkingEmployeeDto patchNotWorkingEmployee(@RequestBody @Valid PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto,
                                                                 @PathVariable int notWorkingEmployeeId) {
        log.debug("Вызван метод patchNotWorkingEmployee");
        return adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, notWorkingEmployeeId);
    }

    @DeleteMapping("/{notWorkingEmployeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotWorkingEmployee(@PathVariable int notWorkingEmployeeId) {
        log.debug("Вызван метод deleteNotWorkingEmployee");
        adminNotWorkingEmployeeService.deleteNotWorkingEmployee(notWorkingEmployeeId);
    }
}
