package ru.yandex.javaemployeecalendar.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.javaemployeecalendar.error.exception.DataConflictException;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;
import ru.yandex.javaemployeecalendar.notworkingemployee.repository.NotWorkingEmployeeRepository;
import ru.yandex.javaemployeecalendar.user.dto.PatchUserDto;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;
import ru.yandex.javaemployeecalendar.user.mapper.UserMapper;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.RoleRepository;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;


@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NotWorkingEmployeeRepository notWorkingEmployeeRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseUserDto postUser(PostUserDto postUserDto) {

        Role roleFromDb = roleRepository.findById(postUserDto.getRoleId()).orElseThrow(() -> new EntityNotFoundException(String.format(ROLE_NOT_FOUND_BY_ID, postUserDto.getRoleId())));

        if (userRepository.existsByEmail(postUserDto.getEmail())) {
            throw new DataConflictException(String.format(USER_WITH_THIS_EMAIL_ALREADY_EXISTS, postUserDto.getEmail()));
        }

        User user = UserMapper.createNewUser(postUserDto, roleFromDb);
        return UserMapper.createResponseUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ResponseUserDto getUserById(int userId) {
        User userFromDb = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
        return UserMapper.createResponseUserDto(userFromDb);
    }


    @Override
    public int getUserWorkingTimeByUserId(int userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId));
        }

        LocalDateTime timeNow = LocalDateTime.now();
        List<NotWorkingEmployee> listWhenEmployeeDidntWork = notWorkingEmployeeRepository.getAllByEmployeeIdAndStartTimeAfterAndEndTimeBefore(userId, timeNow.with(firstDayOfMonth()), timeNow.with(lastDayOfMonth()));
        long count = 0;
        for (NotWorkingEmployee timeWhenNotWork : listWhenEmployeeDidntWork) {
            count += Duration.between(timeWhenNotWork.getStartTime(), timeWhenNotWork.getEndTime()).toDays();
        }
        return (int) (count * 8);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ResponseUserDto> getAllUsers(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return userRepository.findAll(pageRequest).stream().map(UserMapper::createResponseUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseUserDto patchUser(PatchUserDto patchUserDto, int userId) {
        User userFromDb = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));

        if (patchUserDto.getEmail() != null) {
            if (userRepository.existsByEmail(patchUserDto.getEmail())) {
                throw new DataConflictException(String.format(USER_WITH_THIS_EMAIL_ALREADY_EXISTS, patchUserDto.getEmail()));
            }
            userFromDb.setEmail(patchUserDto.getEmail());
        }

        if (patchUserDto.getPassword() != null) {
            userFromDb.setPassword(patchUserDto.getPassword());
        }

        if (patchUserDto.getRoleId() != null) {
            Role role = roleRepository.findById(patchUserDto.getRoleId()).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
            userFromDb.setRole(role);
        }

        if (patchUserDto.getIsAccountNonExpired() != null) {
            userFromDb.setAccountNonExpired(patchUserDto.getIsAccountNonExpired());
        }

        if (patchUserDto.getIsAccountNonLocked() != null) {
            userFromDb.setAccountNonLocked(patchUserDto.getIsAccountNonLocked());
        }

        if (patchUserDto.getIsCredentialsNonExpired() != null) {
            userFromDb.setCredentialsNonExpired(patchUserDto.getIsCredentialsNonExpired());
        }

        if (patchUserDto.getIsEnabled() != null) {
            userFromDb.setEnabled(patchUserDto.getIsEnabled());
        }

        return UserMapper.createResponseUserDto(userFromDb);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId));
        }
        userRepository.deleteById(userId);
    }

}
