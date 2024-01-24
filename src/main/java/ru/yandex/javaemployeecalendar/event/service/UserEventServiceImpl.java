package ru.yandex.javaemployeecalendar.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.error.exception.InvalidRequestException;
import ru.yandex.javaemployeecalendar.event.dto.PatchEventDto;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;
import ru.yandex.javaemployeecalendar.event.mapper.EventMapper;
import ru.yandex.javaemployeecalendar.event.model.Event;
import ru.yandex.javaemployeecalendar.event.repository.EventRepository;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEventDto postEventForWork(PostEventDto postEventDto) {

        // У startTime разница от текущего времени максимум 7 дней, а минимум -8 часов.
        if (LocalDateTime.now().plusDays(7).isBefore(postEventDto.getStartTime()) || LocalDateTime.now().minusHours(8).isAfter(postEventDto.getStartTime())) {
            throw new InvalidRequestException(EVENT_START_DATE_RESTRICTIONS);
        }

        // У endTime разница от текущего времени максимум 14 дней, а минимум 1 день.
        if (LocalDateTime.now().plusDays(14).isBefore(postEventDto.getEndTime()) || LocalDateTime.now().plusDays(1).isAfter(postEventDto.getEndTime())) {
            throw new InvalidRequestException(EVENT_END_DATE_RESTRICTIONS);
        }

        // минимальная разница между startTime и endTime 1 день.
        if (postEventDto.getStartTime().plusDays(1).isAfter(postEventDto.getEndTime())) {
            throw new InvalidRequestException(EVENT_START_AND_END_DATE_RESTRICTIONS);
        }

        User userFromDb = userRepository.findById(postEventDto.getResponsibleId()).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postEventDto.getResponsibleId())));

        Event event = EventMapper.createEvent(postEventDto, userFromDb);

        return EventMapper.createResponseEventDto(eventRepository.save(event));

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ResponseEventDto getEventForWorkById(int eventId) {
        Event eventFromDb = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(String.format(EVENT_NOT_FOUND_BY_ID, eventId)));

        return EventMapper.createResponseEventDto(eventFromDb);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ResponseEventDto> getAllEventsForWork(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventRepository.findAll(pageRequest).stream().map(EventMapper::createResponseEventDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEventDto patchEventForWork(PatchEventDto patchEventDto, int eventId) {

        if (!userRepository.existsById(patchEventDto.getResponsibleId())) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, patchEventDto.getResponsibleId()));
        }

        Event eventFromDb = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(String.format(EVENT_NOT_FOUND_BY_ID, eventId)));

        if (patchEventDto.getName() != null) {
            eventFromDb.setName(patchEventDto.getName());
        }

        if (patchEventDto.getDescription() != null) {
            eventFromDb.setDescription(patchEventDto.getDescription());
        }

        if (patchEventDto.getStartTime() != null) {

            // минимальная разница между startTime и endTime 1 день.
            if ((patchEventDto.getEndTime() != null) &&
                    (patchEventDto.getStartTime().plusDays(1).isAfter(patchEventDto.getEndTime()))) {
                throw new InvalidRequestException(EVENT_START_AND_END_DATE_RESTRICTIONS);
            }

            // У startTime разница от текущего времени максимум 7 дней, а минимум -8 часов.
            if (LocalDateTime.now().plusDays(7).isBefore(patchEventDto.getStartTime()) || LocalDateTime.now().minusHours(8).isAfter(patchEventDto.getStartTime())) {
                throw new InvalidRequestException(EVENT_START_DATE_RESTRICTIONS);
            }

            eventFromDb.setStartTime(patchEventDto.getStartTime());
        }

        if (patchEventDto.getEndTime() != null) {

            // У endTime разница от текущего времени максимум 14 дней, а минимум 1 день.
            if (LocalDateTime.now().plusDays(14).isBefore(patchEventDto.getEndTime()) || LocalDateTime.now().plusDays(1).isAfter(patchEventDto.getEndTime())) {
                throw new InvalidRequestException(EVENT_END_DATE_RESTRICTIONS);
            }

            eventFromDb.setEndTime(patchEventDto.getEndTime());
        }

        if (patchEventDto.getResponsibleId() != null) {

            User userFromDb = userRepository.findById(patchEventDto.getResponsibleId()).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, patchEventDto.getResponsibleId())));

            eventFromDb.setResponsible(userFromDb);
        }

        return EventMapper.createResponseEventDto(eventRepository.save(eventFromDb));

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteEventForWork(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException(String.format(EVENT_NOT_FOUND_BY_ID, eventId));
        }

        eventRepository.deleteById(eventId);
    }

}
