package ru.yandex.javaemployeecalendar.event.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;
import ru.yandex.javaemployeecalendar.event.model.Event;
import ru.yandex.javaemployeecalendar.user.model.User;

@UtilityClass
public class EventMapper {
    public static Event createEvent(PostEventDto postEventDto, User user) {
        return Event.builder()
                .name(postEventDto.getName())
                .description(postEventDto.getDescription())
                .startTime(postEventDto.getStartTime())
                .endTime(postEventDto.getEndTime())
                .responsible(user)
                .build();
    }

    public static ResponseEventDto createResponseEventDto(Event event) {
        return ResponseEventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .responsible(event.getResponsible())
                .build();
    }
}
