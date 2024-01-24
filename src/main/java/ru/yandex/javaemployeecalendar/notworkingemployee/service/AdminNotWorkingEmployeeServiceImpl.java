package ru.yandex.javaemployeecalendar.notworkingemployee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.error.exception.InvalidRequestException;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.mapper.NotWorkingEmployeeMapper;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;
import ru.yandex.javaemployeecalendar.notworkingemployee.repository.NotWorkingEmployeeRepository;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class AdminNotWorkingEmployeeServiceImpl implements AdminNotWorkingEmployeeService {

    private final NotWorkingEmployeeRepository notWorkingEmployeeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseNotWorkingEmployeeDto postNotWorkingEmployee(PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto) {

        // У startTime разница от текущего времени максимум 7 дней, а минимум -8 часов.
        if (LocalDateTime.now().plusDays(7).isBefore(postPatchNotWorkingEmployeeDto.getStartTime()) || LocalDateTime.now().minusHours(8).isAfter(postPatchNotWorkingEmployeeDto.getStartTime())) {
            throw new InvalidRequestException(EVENT_START_DATE_RESTRICTIONS);
        }

        // У endTime разница от текущего времени максимум 14 дней, а минимум 1 день.
        if (LocalDateTime.now().plusDays(14).isBefore(postPatchNotWorkingEmployeeDto.getEndTime()) || LocalDateTime.now().plusDays(1).isAfter(postPatchNotWorkingEmployeeDto.getEndTime())) {
            throw new InvalidRequestException(EVENT_END_DATE_RESTRICTIONS);
        }

        // минимальная разница между startTime и endTime 1 день.
        if (postPatchNotWorkingEmployeeDto.getStartTime().plusDays(1).isAfter(postPatchNotWorkingEmployeeDto.getEndTime())) {
            throw new InvalidRequestException(EVENT_START_AND_END_DATE_RESTRICTIONS);
        }

        User userFromDb = userRepository.findById(postPatchNotWorkingEmployeeDto.getEmployeeId()).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postPatchNotWorkingEmployeeDto.getEmployeeId())));

        NotWorkingEmployee notWorkingEmployee = NotWorkingEmployeeMapper.createNotWorkingEmployee(postPatchNotWorkingEmployeeDto, userFromDb);

        return NotWorkingEmployeeMapper.createResponseNotWorkingEmployee(notWorkingEmployeeRepository.save(notWorkingEmployee));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ResponseNotWorkingEmployeeDto getCurrentNotWorkingEmployeeByUserId(int userId) {
        NotWorkingEmployee notWorkingEmployeeFromDb = notWorkingEmployeeRepository.getByEmployeeIdAndStartTimeAfterAndEndTimeBefore(userId, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_USER_ID, userId)));

        return NotWorkingEmployeeMapper.createResponseNotWorkingEmployee(notWorkingEmployeeFromDb);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ResponseNotWorkingEmployeeDto> getAllNotWorkingEmployee(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return notWorkingEmployeeRepository.findAll(pageRequest).stream().map(NotWorkingEmployeeMapper::createResponseNotWorkingEmployee).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseNotWorkingEmployeeDto patchNotWorkingEmployee(PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto, int notWorkingEmployeeId) {

        if (!userRepository.existsById(postPatchNotWorkingEmployeeDto.getEmployeeId())) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postPatchNotWorkingEmployeeDto.getEmployeeId()));
        }

        NotWorkingEmployee notWorkingEmployeeFromDb = notWorkingEmployeeRepository.findById(notWorkingEmployeeId).orElseThrow(() -> new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_ID, notWorkingEmployeeId)));

        if (postPatchNotWorkingEmployeeDto.getDescription() != null) {
            notWorkingEmployeeFromDb.setDescription(postPatchNotWorkingEmployeeDto.getDescription());
        }

        if (postPatchNotWorkingEmployeeDto.getStartTime() != null) {

            // минимальная разница между startTime и endTime 1 день.
            if ((postPatchNotWorkingEmployeeDto.getEndTime() != null) &&
                    (postPatchNotWorkingEmployeeDto.getStartTime().plusDays(1).isAfter(postPatchNotWorkingEmployeeDto.getEndTime()))) {
                throw new InvalidRequestException(EVENT_START_AND_END_DATE_RESTRICTIONS);
            }

            // У startTime разница от текущего времени максимум 7 дней, а минимум -8 часов.
            if (LocalDateTime.now().plusDays(7).isBefore(postPatchNotWorkingEmployeeDto.getStartTime()) || LocalDateTime.now().minusHours(8).isAfter(postPatchNotWorkingEmployeeDto.getStartTime())) {
                throw new InvalidRequestException(EVENT_START_DATE_RESTRICTIONS);
            }

            notWorkingEmployeeFromDb.setStartTime(postPatchNotWorkingEmployeeDto.getStartTime());
        }

        if (postPatchNotWorkingEmployeeDto.getEndTime() != null) {

            // У endTime разница от текущего времени максимум 14 дней, а минимум 1 день.
            if (LocalDateTime.now().plusDays(14).isBefore(postPatchNotWorkingEmployeeDto.getEndTime()) || LocalDateTime.now().plusDays(1).isAfter(postPatchNotWorkingEmployeeDto.getEndTime())) {
                throw new InvalidRequestException(EVENT_END_DATE_RESTRICTIONS);
            }

            notWorkingEmployeeFromDb.setEndTime(postPatchNotWorkingEmployeeDto.getEndTime());
        }

        if (postPatchNotWorkingEmployeeDto.getEmployeeId() != null) {

            User userFromDb = userRepository.findById(postPatchNotWorkingEmployeeDto.getEmployeeId()).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postPatchNotWorkingEmployeeDto.getEmployeeId())));

            notWorkingEmployeeFromDb.setEmployee(userFromDb);

        }
        return NotWorkingEmployeeMapper.createResponseNotWorkingEmployee(notWorkingEmployeeRepository.save(notWorkingEmployeeFromDb));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteNotWorkingEmployee(int notWorkingEmployeeId) {
        if (!notWorkingEmployeeRepository.existsById(notWorkingEmployeeId)) {
            throw new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_ID, notWorkingEmployeeId));
        }

        notWorkingEmployeeRepository.deleteById(notWorkingEmployeeId);
    }
}
