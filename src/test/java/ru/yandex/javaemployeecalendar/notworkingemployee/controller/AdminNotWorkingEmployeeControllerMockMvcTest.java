package ru.yandex.javaemployeecalendar.notworkingemployee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.PostPatchNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.dto.ResponseNotWorkingEmployeeDto;
import ru.yandex.javaemployeecalendar.notworkingemployee.service.AdminNotWorkingEmployeeService;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.RoleEnum;
import ru.yandex.javaemployeecalendar.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.javaemployeecalendar.error.constants.ErrorConstants.FORMATTER;

@WebMvcTest(controllers = AdminNotWorkingEmployeeController.class)
class AdminNotWorkingEmployeeControllerMockMvcTest {

    @MockBean
    AdminNotWorkingEmployeeService adminNotWorkingEmployeeService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postNotWorkingEmployee() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(user.getId(), currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto = new ResponseNotWorkingEmployeeDto(1, user, currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");

        when(adminNotWorkingEmployeeService.postNotWorkingEmployee(Mockito.any(PostPatchNotWorkingEmployeeDto.class)))
                .thenReturn(responseNotWorkingEmployeeDto);

        mvc.perform(post("/admin/not-working")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postPatchNotWorkingEmployeeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseNotWorkingEmployeeDto.getId())))
                .andExpect(jsonPath("$.employee.email", is(responseNotWorkingEmployeeDto.getEmployee().getEmail())))
                .andExpect(jsonPath("$.employee.password", is(responseNotWorkingEmployeeDto.getEmployee().getPassword())))
                .andExpect(jsonPath("$.employee.role.id", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getId())))
                .andExpect(jsonPath("$.employee.role.name", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getName().toString())))
                .andExpect(jsonPath("$.employee.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.employee.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.enabled", is(true)))
                .andExpect(jsonPath("$.startTime", is(responseNotWorkingEmployeeDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseNotWorkingEmployeeDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.description", is("Заболел душевной болью, спидом и раком")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCurrentNotWorkingEmployeeByUserId() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto = new ResponseNotWorkingEmployeeDto(1, user, currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");

        when(adminNotWorkingEmployeeService.getCurrentNotWorkingEmployeeByUserId(Mockito.anyInt()))
                .thenReturn(responseNotWorkingEmployeeDto);

        mvc.perform(get("/admin/not-working/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseNotWorkingEmployeeDto.getId())))
                .andExpect(jsonPath("$.employee.email", is(responseNotWorkingEmployeeDto.getEmployee().getEmail())))
                .andExpect(jsonPath("$.employee.password", is(responseNotWorkingEmployeeDto.getEmployee().getPassword())))
                .andExpect(jsonPath("$.employee.role.id", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getId())))
                .andExpect(jsonPath("$.employee.role.name", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getName().toString())))
                .andExpect(jsonPath("$.employee.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.employee.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.enabled", is(true)))
                .andExpect(jsonPath("$.startTime", is(responseNotWorkingEmployeeDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseNotWorkingEmployeeDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.description", is("Заболел душевной болью, спидом и раком")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllNotWorkingEmployee() throws Exception {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(1, RoleEnum.USER);
        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);
        User user2 = new User(2, "vitekb652@gmail.com", "12345678910", role2, false, false, false, false);

        LocalDateTime currentTime = LocalDateTime.now();

        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto1 = new ResponseNotWorkingEmployeeDto(1, user1, currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto2 = new ResponseNotWorkingEmployeeDto(2, user2, currentTime.minusDays(8), currentTime.minusDays(1), "Пал духом, но не телом, разумом, но не плотью");

        List<ResponseNotWorkingEmployeeDto> responseNotWorkingEmployeeDtoList = List.of(responseNotWorkingEmployeeDto1, responseNotWorkingEmployeeDto2);

        when(adminNotWorkingEmployeeService.getAllNotWorkingEmployee(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(responseNotWorkingEmployeeDtoList);

        mvc.perform(get("/admin/not-working")
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseNotWorkingEmployeeDto1.getId())))
                .andExpect(jsonPath("$[0].employee.email", is(responseNotWorkingEmployeeDto1.getEmployee().getEmail())))
                .andExpect(jsonPath("$[0].employee.password", is(responseNotWorkingEmployeeDto1.getEmployee().getPassword())))
                .andExpect(jsonPath("$[0].employee.role.id", is(responseNotWorkingEmployeeDto1.getEmployee().getRole().getId())))
                .andExpect(jsonPath("$[0].employee.role.name", is(responseNotWorkingEmployeeDto1.getEmployee().getRole().getName().toString())))
                .andExpect(jsonPath("$[0].employee.accountNonExpired", is(true)))
                .andExpect(jsonPath("$[0].employee.accountNonLocked", is(true)))
                .andExpect(jsonPath("$[0].employee.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$[0].employee.enabled", is(true)))
                .andExpect(jsonPath("$[0].startTime", is(responseNotWorkingEmployeeDto1.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$[0].endTime", is(responseNotWorkingEmployeeDto1.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$[0].description", is("Заболел душевной болью, спидом и раком")))
                .andExpect(jsonPath("$[1].id", is(responseNotWorkingEmployeeDto2.getId())))
                .andExpect(jsonPath("$[1].employee.email", is(responseNotWorkingEmployeeDto2.getEmployee().getEmail())))
                .andExpect(jsonPath("$[1].employee.password", is(responseNotWorkingEmployeeDto2.getEmployee().getPassword())))
                .andExpect(jsonPath("$[1].employee.role.id", is(responseNotWorkingEmployeeDto2.getEmployee().getRole().getId())))
                .andExpect(jsonPath("$[1].employee.role.name", is(responseNotWorkingEmployeeDto2.getEmployee().getRole().getName().toString())))
                .andExpect(jsonPath("$[1].employee.accountNonExpired", is(false)))
                .andExpect(jsonPath("$[1].employee.accountNonLocked", is(false)))
                .andExpect(jsonPath("$[1].employee.credentialsNonExpired", is(false)))
                .andExpect(jsonPath("$[1].employee.enabled", is(false)))
                .andExpect(jsonPath("$[1].startTime", is(responseNotWorkingEmployeeDto2.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$[1].endTime", is(responseNotWorkingEmployeeDto2.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$[1].description", is("Пал духом, но не телом, разумом, но не плотью")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void patchNotWorkingEmployee() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostPatchNotWorkingEmployeeDto postPatchNotWorkingEmployeeDto = new PostPatchNotWorkingEmployeeDto(user.getId(), currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");
        ResponseNotWorkingEmployeeDto responseNotWorkingEmployeeDto = new ResponseNotWorkingEmployeeDto(1, user, currentTime, currentTime.plusDays(7), "Заболел душевной болью, спидом и раком");

        when(adminNotWorkingEmployeeService.patchNotWorkingEmployee(Mockito.any(PostPatchNotWorkingEmployeeDto.class), Mockito.anyInt()))
                .thenReturn(responseNotWorkingEmployeeDto);

        mvc.perform(patch("/admin/not-working/{userId}", 1)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postPatchNotWorkingEmployeeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseNotWorkingEmployeeDto.getId())))
                .andExpect(jsonPath("$.employee.email", is(responseNotWorkingEmployeeDto.getEmployee().getEmail())))
                .andExpect(jsonPath("$.employee.password", is(responseNotWorkingEmployeeDto.getEmployee().getPassword())))
                .andExpect(jsonPath("$.employee.role.id", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getId())))
                .andExpect(jsonPath("$.employee.role.name", is(responseNotWorkingEmployeeDto.getEmployee().getRole().getName().toString())))
                .andExpect(jsonPath("$.employee.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.employee.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.employee.enabled", is(true)))
                .andExpect(jsonPath("$.startTime", is(responseNotWorkingEmployeeDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseNotWorkingEmployeeDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.description", is("Заболел душевной болью, спидом и раком")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteNotWorkingEmployee() throws Exception {

        mvc.perform(delete("/admin/not-working/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(adminNotWorkingEmployeeService, Mockito.times(1))
                .deleteNotWorkingEmployee(1);
    }
}