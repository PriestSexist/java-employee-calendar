package ru.yandex.javaemployeecalendar.notworkingemployee.service;

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
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;
import ru.yandex.javaemployeecalendar.notworkingemployee.repository.NotWorkingEmployeeRepository;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.RoleEnum;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;

@ExtendWith(MockitoExtension.class)
class AdminNotWorkingEmployeeServiceImplUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    NotWorkingEmployeeRepository notWorkingEmployeeRepository;
    AdminNotWorkingEmployeeService adminNotWorkingEmployeeService;

    @BeforeEach
    void generator() {
        adminNotWorkingEmployeeService = new AdminNotWorkingEmployeeServiceImpl(notWorkingEmployeeRepository, userRepository);
    }

    @Test
    void postNotWorkingEmployeeWrongStartTime() {

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime.plusDays(8), currentTime.plusDays(9), "Заболел душевной болью, спидом и раком");

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto));
    }


    @Test
    void postNotWorkingEmployeeWrongEndTime() {

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime, currentTime.plusHours(1), "Заболел душевной болью, спидом и раком");

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto));
    }

    @Test
    void postNotWorkingEmployeeWrongStartTimeAndEndTime() {

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime.plusDays(5), currentTime.plusDays(4), "Заболел душевной болью, спидом и раком");

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto));
    }

    @Test
    void postNotWorkingEmployeeUserNotFound() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(user.getId(), currentTime.plusDays(4), currentTime.plusDays(5), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, postPatchNotWorkingEmployeeDto.getEmployeeId())));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto));
    }

    @Test
    void postNotWorkingEmployee() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(user.getId(), currentTime.plusDays(1), currentTime.plusDays(2), "Заболел душевной болью, спидом и раком");
        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Болел");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto1 = new ResponseNotWorkingEmployeeDto(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Болел");

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(notWorkingEmployeeRepository.save(Mockito.any(NotWorkingEmployee.class)))
                .thenReturn(notWorkingEmployee1);

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto2 = adminNotWorkingEmployeeService.postNotWorkingEmployee(postPatchNotWorkingEmployeeDto);

        Assertions.assertEquals(responseNotWorkingEmployeeDto1, responseNotWorkingEmployeeDto2);
    }

    @Test
    void getCurrentNotWorkingEmployeeByUserIdNotWorkingEmployeeNotFoundById() {

        Mockito.when(notWorkingEmployeeRepository.getByEmployeeIdAndStartTimeAfterAndEndTimeBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenThrow(new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_USER_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminNotWorkingEmployeeService.getCurrentNotWorkingEmployeeByUserId(1));
    }

    @Test
    void getCurrentNotWorkingEmployeeByUserId() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        NotWorkingEmployee notWorkingEmployee = new NotWorkingEmployee(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Болел");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto1 = new ResponseNotWorkingEmployeeDto(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Болел");

        Mockito.when(notWorkingEmployeeRepository.getByEmployeeIdAndStartTimeAfterAndEndTimeBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(Optional.of(notWorkingEmployee));

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto2 = adminNotWorkingEmployeeService.getCurrentNotWorkingEmployeeByUserId(1);

        Assertions.assertEquals(responseNotWorkingEmployeeDto1, responseNotWorkingEmployeeDto2);
    }

    @Test
    void getAllNotWorkingEmployee() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime localDateTimeStartTime1 = LocalDateTime.of(2023, 12, 1, 10, 10, 10);
        LocalDateTime localDateTimeEndTime1 = LocalDateTime.of(2023, 12, 2, 10, 10, 10);
        LocalDateTime localDateTimeStartTime2 = LocalDateTime.of(2023, 12, 4, 10, 10, 10);
        LocalDateTime localDateTimeEndTime2 = LocalDateTime.of(2023, 12, 5, 10, 10, 10);

        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user, localDateTimeStartTime1, localDateTimeEndTime1, "Болел");
        NotWorkingEmployee notWorkingEmployee2 = new NotWorkingEmployee(2, user, localDateTimeStartTime2, localDateTimeEndTime2, "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto1 = new ResponseNotWorkingEmployeeDto(1, user, localDateTimeStartTime1, localDateTimeEndTime1, "Болел");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto2 = new ResponseNotWorkingEmployeeDto(2, user, localDateTimeStartTime2, localDateTimeEndTime2, "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        List<ResponseNotWorkingEmployeeDto> responseNotWorkingEmployeeDtoList1 = List.of(responseNotWorkingEmployeeDto1, responseNotWorkingEmployeeDto2);
        Page<NotWorkingEmployee> page = new PageImpl<>(List.of(notWorkingEmployee1, notWorkingEmployee2));

        Mockito.when(notWorkingEmployeeRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        List<ResponseNotWorkingEmployeeDto> responseNotWorkingEmployeeDtoList2 = adminNotWorkingEmployeeService.getAllNotWorkingEmployee(0, 10);

        Assertions.assertEquals(responseNotWorkingEmployeeDtoList1, responseNotWorkingEmployeeDtoList2);
    }

    @Test
    void patchNotWorkingEmployeeEmployeeNotFound() {

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(2, currentTime.plusDays(4), currentTime.plusDays(5), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1));

    }

    @Test
    void patchNotWorkingEmployeeNotWorkingEmployeeNotFoundById() {

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(2, currentTime.plusDays(4), currentTime.plusDays(5), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1));
    }

    @Test
    void patchNotWorkingEmployeeWrongStartTimeAndEndTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        NotWorkingEmployee notWorkingEmployee = new NotWorkingEmployee(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime, currentTime.plusHours(1), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(notWorkingEmployee));

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1));

    }

    @Test
    void patchNotWorkingEmployeeWrongStartTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime.plusDays(9), currentTime.plusDays(10), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(notWorkingEmployee1));

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1));
    }

    @Test
    void patchNotWorkingEmployeeWrongEndTimeException() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user, currentTime.plusDays(1), currentTime.plusDays(2), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(1, currentTime.minusHours(4), currentTime.plusHours(23), "Заболел душевной болью, спидом и раком");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(notWorkingEmployee1));

        Assertions.assertThrows(InvalidRequestException.class, () -> adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1));
    }

    @Test
    void patchNotWorkingEmployee() {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(2, RoleEnum.USER);
        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);
        User user2 = new User(2, "vitekb652@gmail.com", "12345678910", role2, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user1, currentTime.plusDays(1), currentTime.plusDays(2), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");
        NotWorkingEmployee notWorkingEmployee2 = new NotWorkingEmployee(2, user2, currentTime.plusDays(4), currentTime.plusDays(5), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(2, currentTime.plusDays(4), currentTime.plusDays(5), "Заболел душевной болью, спидом и раком");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto1 = new ResponseNotWorkingEmployeeDto(2, user2, currentTime.plusDays(4), currentTime.plusDays(5), "Ghbvbnt vtyz yf hf,jne gj;fkeqcnf)");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(notWorkingEmployee1));

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user2));

        Mockito.when(notWorkingEmployeeRepository.save(Mockito.any(NotWorkingEmployee.class)))
                .thenReturn(notWorkingEmployee2);

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto2 = adminNotWorkingEmployeeService.patchNotWorkingEmployee(postPatchNotWorkingEmployeeDto, 1);

        Assertions.assertEquals(responseNotWorkingEmployeeDto1, responseNotWorkingEmployeeDto2);
    }

    @Test
    void deleteUserNotFoundById() {

        Mockito.when(notWorkingEmployeeRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 2)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminNotWorkingEmployeeService.deleteNotWorkingEmployee(2));
    }

    @Test
    void deleteNotWorkingEmployee() {

        Mockito.when(notWorkingEmployeeRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        adminNotWorkingEmployeeService.deleteNotWorkingEmployee(1);

        Mockito.verify(notWorkingEmployeeRepository, Mockito.times(1))
                .deleteById(1);
    }
}