package ru.yandex.javaemployeecalendar.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.yandex.javaemployeecalendar.error.exception.DataConflictException;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.notworkingemployee.model.NotWorkingEmployee;
import ru.yandex.javaemployeecalendar.notworkingemployee.repository.NotWorkingEmployeeRepository;
import ru.yandex.javaemployeecalendar.user.dto.PatchUserDto;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.RoleEnum;
import ru.yandex.javaemployeecalendar.user.model.User;
import ru.yandex.javaemployeecalendar.user.repository.RoleRepository;
import ru.yandex.javaemployeecalendar.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceImplUnitTest {
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    NotWorkingEmployeeRepository notWorkingEmployeeRepository;
    AdminUserService adminUserService;

    @BeforeEach
    void generator() {
        adminUserService = new AdminUserServiceImpl(userRepository, roleRepository, notWorkingEmployeeRepository);
    }

    @Test
    void postUser() {

        Role role = new Role(1, RoleEnum.ADMIN);
        ResponseUserDto responseUserDto1 = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "123456789", role.getId());
        User user = new User(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        Mockito.when(roleRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(role));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        ResponseUserDto responseUserDto2 = adminUserService.postUser(postUserDto);

        Assertions.assertEquals(responseUserDto1, responseUserDto2);
    }

    @Test
    void postUserRoleNotFound() {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "123456789", role.getId());

        Mockito.when(roleRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(ROLE_NOT_FOUND_BY_ID, postUserDto.getRoleId())));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.postUser(postUserDto));
    }

    @Test
    void postUserExists() {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "123456789", role.getId());

        Mockito.when(roleRepository.findById(Mockito.anyInt()))
                .thenThrow(new DataConflictException(String.format(USER_WITH_THIS_EMAIL_ALREADY_EXISTS, postUserDto.getEmail())));

        Assertions.assertThrows(DataConflictException.class, () -> adminUserService.postUser(postUserDto));
    }

    @Test
    void getUserNotFound() {

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 2)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.getUserById(2));
    }

    @Test
    void getUserById() {

        Role role = new Role(1, RoleEnum.ADMIN);
        ResponseUserDto responseUserDto1 = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);
        User user = new User(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        ResponseUserDto responseUserDto2 = adminUserService.getUserById(1);

        Assertions.assertEquals(responseUserDto1, responseUserDto2);
    }

    @Test
    void getUserWorkingTimeByUserIdUserNotFound() {

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 2)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.getUserWorkingTimeByUserId(2));
    }

    @Test
    void getUserWorkingTimeByUserId() {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime localDateTimeStartTime1 = LocalDateTime.of(2023, 12, 1, 10, 10, 10);
        LocalDateTime localDateTimeEndTime1 = LocalDateTime.of(2023, 12, 2, 10, 10, 10);
        LocalDateTime localDateTimeStartTime2 = LocalDateTime.of(2023, 12, 4, 10, 10, 10);
        LocalDateTime localDateTimeEndTime2 = LocalDateTime.of(2023, 12, 5, 10, 10, 10);

        NotWorkingEmployee notWorkingEmployee1 = new NotWorkingEmployee(1, user, localDateTimeStartTime1, localDateTimeEndTime1, "Болел");
        NotWorkingEmployee notWorkingEmployee2 = new NotWorkingEmployee(2, user, localDateTimeStartTime2, localDateTimeEndTime2, "Болел");

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        Mockito.when(notWorkingEmployeeRepository.getAllByEmployeeIdAndStartTimeAfterAndEndTimeBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(notWorkingEmployee1, notWorkingEmployee2));

        Integer hours = adminUserService.getUserWorkingTimeByUserId(1);

        Assertions.assertEquals(hours, 16);
    }

    @Test
    void getAllUsers() {

        Role role = new Role(1, RoleEnum.ADMIN);

        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);
        User user2 = new User(2, "vitekb652@gmail.com", "12345678910", role, true, true, true, true);

        ResponseUserDto responseUserDto1 = new ResponseUserDto(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);
        ResponseUserDto responseUserDto2 = new ResponseUserDto(2, "vitekb652@gmail.com", "12345678910", role, true, true, true, true);

        List<ResponseUserDto> responseUserDtoList = List.of(responseUserDto1, responseUserDto2);
        Page<User> page = new PageImpl<>(List.of(user1, user2));

        Mockito.when(userRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(page);

        List<ResponseUserDto> list = adminUserService.getAllUsers(0, 10);

        Assertions.assertEquals(list, responseUserDtoList);
    }

    @Test
    void patchUserNotFoundById() {

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 2)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.getUserById(2));
    }

    @Test
    void patchUserEmailAlreadyExists() {

        Role role1 = new Role(1, RoleEnum.ADMIN);

        PatchUserDto patchUserDto = new PatchUserDto("vitekb652@gmail.com", "12345678910", 2, false, false, false, false);

        User user = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString()))
                .thenThrow(new DataConflictException(String.format(USER_WITH_THIS_EMAIL_ALREADY_EXISTS, patchUserDto.getEmail())));


        Assertions.assertThrows(DataConflictException.class, () -> adminUserService.patchUser(patchUserDto, 1));
    }

    @Test
    void patchUserRoleNotFoundException() {

        Role role1 = new Role(1, RoleEnum.ADMIN);

        PatchUserDto patchUserDto = new PatchUserDto("vitekb652@gmail.com", "12345678910", 2, false, false, false, false);

        User user = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(roleRepository.findById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 1)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.patchUser(patchUserDto, 1));
    }

    @Test
    void patchUser() {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(2, RoleEnum.USER);

        PatchUserDto patchUserDto = new PatchUserDto("vitekb652@gmail.com", "12345678910", 2, false, false, false, false);
        ResponseUserDto responseUserDto1 = new ResponseUserDto(1, "vitekb652@gmail.com", "12345678910", role2, false, false, false, false);

        User user = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(roleRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(role2));

        ResponseUserDto responseUserDto2 = adminUserService.patchUser(patchUserDto, 1);

        Assertions.assertEquals(responseUserDto1, responseUserDto2);
    }

    @Test
    void deleteUserNotFoundById() {

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(String.format(USER_NOT_FOUND_BY_ID, 2)));

        Assertions.assertThrows(EntityNotFoundException.class, () -> adminUserService.deleteUser(2));
    }

    @Test
    void deleteUser() {

        Mockito.when(userRepository.existsById(Mockito.anyInt()))
                .thenReturn(true);

        adminUserService.deleteUser(1);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(1);

    }
}