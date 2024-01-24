package ru.yandex.javaemployeecalendar.event.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.error.exception.InvalidRequestException;
import ru.yandex.javaemployeecalendar.event.dto.PatchEventDto;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;
import ru.yandex.javaemployeecalendar.event.model.Event;
import ru.yandex.javaemployeecalendar.event.repository.EventRepository;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.RoleEnum;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;

@ExtendWith(MockitoExtension.class)
class UserEventServiceImplUnitTest {

    @Mock
    EventRepository eventRepository;
    @Mock
    UserRepository userRepository;
    UserEventService userEventService;

    @BeforeEach
    void generator() {
        userEventService = new UserEventServiceImpl(eventRepository, userRepository);
    }

    @Test
    void postEventForWorkWrongStartTime() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(8), currentTime.plusDays(9), user.getId());

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.postEventForWork(postEventDto));
    }


    @Test
    void postEventForWorkWrongEndTime() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime, currentTime.plusHours(1), user.getId());

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.postEventForWork(postEventDto));
    }

    @Test
    void postEventForWorkWrongStartTimeAndEndTime() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(5), currentTime.plusDays(4), user.getId());

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.postEventForWork(postEventDto));
    }

    @Test
    void postEventForWorkResponsibleNotFoundById() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(4), currentTime.plusDays(5), user.getId());

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postEventDto.getResponsibleId())));

        Assertions.assertThrows(EntityNotFoundException.class, () -> userEventService.postEventForWork(postEventDto));
    }

    @Test
    void postEventForWork() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);
        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user.getId());
        ResponseEventDto responseEventDto1 = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(eventRepository.save(Mockito.any(Event.class)))
                .thenReturn(event);

        ResponseEventDto responseEventDto2 = userEventService.postEventForWork(postEventDto);

        Assertions.assertEquals(responseEventDto2, responseEventDto1);

    }

    @Test
    void getEventForWorkByIdEventForWorkNotFoundById() {

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_USER_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> userEventService.getEventForWorkById(1));
    }

    @Test
    void getEventForWorkById() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);
        ResponseEventDto responseEventDto1 = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(event));

        ResponseEventDto responseEventDto2 = userEventService.getEventForWorkById(1);

        Assertions.assertEquals(responseEventDto2, responseEventDto1);
    }

    @Test
    void getAllEventsForWork() {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(2, RoleEnum.USER);
        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);
        User user2 = new User(1, "vitekb652@gmail.com", "12345678910", role2, false, false, false, false);

        LocalDateTime localDateTimeStartTime1 = LocalDateTime.of(2023, 12, 1, 10, 10, 10);
        LocalDateTime localDateTimeEndTime1 = LocalDateTime.of(2023, 12, 2, 10, 10, 10);
        LocalDateTime localDateTimeStartTime2 = LocalDateTime.of(2023, 12, 4, 10, 10, 10);
        LocalDateTime localDateTimeEndTime2 = LocalDateTime.of(2023, 12, 5, 10, 10, 10);

        Event event1 = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", localDateTimeStartTime1, localDateTimeEndTime1, user1);
        Event event2 = new Event(1, "Imperial fists angels of Emperor", "For the Emperor, for Terra", localDateTimeStartTime2, localDateTimeEndTime2, user2);

        ResponseEventDto responseEventDto1 = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", localDateTimeStartTime1, localDateTimeEndTime1, user1);
        ResponseEventDto responseEventDto2 = new ResponseEventDto(1, "Imperial fists angels of Emperor", "For the Emperor, for Terra", localDateTimeStartTime2, localDateTimeEndTime2, user2);

        List<ResponseEventDto> responseEventDtoList1 = List.of(responseEventDto1, responseEventDto2);
        Page<Event> page = new PageImpl<>(List.of(event1, event2));

        Mockito.when(eventRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        List<ResponseEventDto> responseEventDtoList2 = userEventService.getAllEventsForWork(0, 10);

        Assertions.assertEquals(responseEventDtoList1, responseEventDtoList2);
    }

    @Test
    void patchEventForWorkResponsibleNotFound() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user.getId());

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> userEventService.patchEventForWork(patchEventDto, 1));

    }

    @Test
    void patchEventForWorkEventNotFoundById() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user.getId());

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(EVENT_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> userEventService.patchEventForWork(patchEventDto, 1));
    }

    @Test
    void patchEventForWorkWrongStartTimeAndEndTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime, currentTime.plusHours(1), user.getId());

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(event));

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.patchEventForWork(patchEventDto, 1));

    }

    @Test
    void ppatchEventForWorkWrongStartTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(9), currentTime.plusHours(10), user.getId());

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(event));

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.patchEventForWork(patchEventDto, 1));
    }

    @Test
    void patchEventForWorkWrongEndTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.minusHours(4), currentTime.plusHours(23), user.getId());

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(event));

        Assertions.assertThrows(InvalidRequestException.class, () -> userEventService.patchEventForWork(patchEventDto, 1));
    }

    @Test
    void patchEventForWork() {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(2, RoleEnum.USER);
        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);
        User user2 = new User(1, "vitekb652@gmail.com", "12345678910", role2, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        Event event1 = new Event(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user1);
        Event event2 = new Event(2, "Imperial fists angels of Emperor", "For the Emperor, for Terra", currentTime.plusDays(4), currentTime.plusDays(5), user2);

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user1.getId());
        ResponseEventDto responseEventDto1 = new ResponseEventDto(2, "Imperial fists angels of Emperor", "For the Emperor, for Terra", currentTime.plusDays(4), currentTime.plusDays(5), user2);

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(eventRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(event1));

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user2));

        Mockito.when(eventRepository.save(Mockito.any(Event.class)))
                .thenReturn(event2);

        ResponseEventDto responseEventDto2 = userEventService.patchEventForWork(patchEventDto, 1);

        Assertions.assertEquals(responseEventDto1, responseEventDto2);
    }

    @Test
    void deleteEventForWorkNotFoundById() {

        Mockito.when(eventRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(EVENT_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> userEventService.deleteEventForWork(2));
    }

    @Test
    void deleteEventForWork() {

        Mockito.when(eventRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        userEventService.deleteEventForWork(1);

        Mockito.verify(eventRepository, Mockito.times(1))
                .deleteById(1);

    }
}