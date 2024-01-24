package ru.yandex.javaemployeecalendar.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.javaemployeecalendar.event.dto.PatchEventDto;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;
import ru.yandex.javaemployeecalendar.event.service.UserEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/event")
@Validated
public class UserEventController {

    private final UserEventService userEventService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEventDto postEventForWork(@RequestBody @Valid PostEventDto postEventDto) {
        log.debug("Вызван метод postEventForWork");
        return userEventService.postEventForWork(postEventDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEventDto getEventForWorkById(@PathVariable int eventId) {
        log.debug("Вызван метод getEventForWorkById");
        return userEventService.getEventForWorkById(eventId);
    }

    @GetMapping()
    public List<ResponseEventDto> getAllEventsForWork(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.debug("Вызван метод getAllEventsForWorkById");
        return userEventService.getAllEventsForWork(from, size);
    }


    @PatchMapping("/{eventId}")
    public ResponseEventDto patchEventForWork(@RequestBody @Valid PatchEventDto patchEventDto,
                                              @PathVariable int eventId) {
        log.debug("Вызван метод patchEventForWork");
        return userEventService.patchEventForWork(patchEventDto, eventId);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventForWork(@PathVariable int eventId) {
        log.debug("Вызван метод deleteEventForWork");
        userEventService.deleteEventForWork(eventId);
    }

}
