package ru.yandex.javaemployeecalendar.event.service;

import ru.yandex.javaemployeecalendar.event.dto.PatchEventDto;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;

import java.util.List;

public interface UserEventService {
    ResponseEventDto postEventForWork(PostEventDto postEventDto);

    ResponseEventDto getEventForWorkById(int eventId);

    ResponseEventDto patchEventForWork(PatchEventDto patchEventDto, int eventId);

    void deleteEventForWork(int eventId);

    List<ResponseEventDto> getAllEventsForWork(int from, int size);
}
