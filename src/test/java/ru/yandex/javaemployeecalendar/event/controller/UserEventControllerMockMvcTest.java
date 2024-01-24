package ru.yandex.javaemployeecalendar.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.javaemployeecalendar.event.dto.PatchEventDto;
import ru.yandex.javaemployeecalendar.event.dto.PostEventDto;
import ru.yandex.javaemployeecalendar.event.dto.ResponseEventDto;
import ru.yandex.javaemployeecalendar.event.service.UserEventService;
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

@WebMvcTest(controllers = UserEventController.class)
class UserEventControllerMockMvcTest {

    @MockBean
    UserEventService userEventService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postEventForWork() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PostEventDto postEventDto = new PostEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user.getId());
        ResponseEventDto responseEventDto = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        when(userEventService.postEventForWork(Mockito.any(PostEventDto.class)))
                .thenReturn(responseEventDto);

        mvc.perform(post("/user/event")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseEventDto.getId())))
                .andExpect(jsonPath("$.name", is(responseEventDto.getName())))
                .andExpect(jsonPath("$.description", is(responseEventDto.getDescription())))
                .andExpect(jsonPath("$.startTime", is(responseEventDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseEventDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.responsible.id", is(user.getId())))
                .andExpect(jsonPath("$.responsible.email", is(user.getEmail())))
                .andExpect(jsonPath("$.responsible.password", is(user.getPassword())))
                .andExpect(jsonPath("$.responsible.role.id", is(user.getRole().getId())))
                .andExpect(jsonPath("$.responsible.role.name", is(user.getRole().getName().toString())))
                .andExpect(jsonPath("$.responsible.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.responsible.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.enabled", is(true)));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getEventForWorkById() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        ResponseEventDto responseEventDto = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        when(userEventService.getEventForWorkById(Mockito.anyInt()))
                .thenReturn(responseEventDto);

        mvc.perform(get("/user/event/{eventId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseEventDto.getId())))
                .andExpect(jsonPath("$.name", is(responseEventDto.getName())))
                .andExpect(jsonPath("$.description", is(responseEventDto.getDescription())))
                .andExpect(jsonPath("$.startTime", is(responseEventDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseEventDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.responsible.id", is(user.getId())))
                .andExpect(jsonPath("$.responsible.email", is(user.getEmail())))
                .andExpect(jsonPath("$.responsible.password", is(user.getPassword())))
                .andExpect(jsonPath("$.responsible.role.id", is(user.getRole().getId())))
                .andExpect(jsonPath("$.responsible.role.name", is(user.getRole().getName().toString())))
                .andExpect(jsonPath("$.responsible.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.responsible.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.enabled", is(true)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllEventsForWork() throws Exception {

        Role role1 = new Role(1, RoleEnum.ADMIN);
        Role role2 = new Role(1, RoleEnum.USER);
        User user1 = new User(1, "vitekb651@gmail.com", "123456789", role1, true, true, true, true);
        User user2 = new User(2, "vitekb652@gmail.com", "12345678910", role2, false, false, false, false);

        LocalDateTime currentTime = LocalDateTime.now();

        ResponseEventDto responseEventDto1 = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user1);
        ResponseEventDto responseEventDto2 = new ResponseEventDto(2, "Imperial fists angels of Emperor", "For the Emperor, for Terra", currentTime.plusDays(4), currentTime.plusDays(5), user2);

        List<ResponseEventDto> responseNotWorkingEmployeeDtoList = List.of(responseEventDto1, responseEventDto2);

        when(userEventService.getAllEventsForWork(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(responseNotWorkingEmployeeDtoList);

        mvc.perform(get("/user/event")
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseEventDto1.getId())))
                .andExpect(jsonPath("$[0].name", is(responseEventDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(responseEventDto1.getDescription())))
                .andExpect(jsonPath("$[0].startTime", is(responseEventDto1.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$[0].endTime", is(responseEventDto1.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$[0].responsible.id", is(user1.getId())))
                .andExpect(jsonPath("$[0].responsible.email", is(user1.getEmail())))
                .andExpect(jsonPath("$[0].responsible.password", is(user1.getPassword())))
                .andExpect(jsonPath("$[0].responsible.role.id", is(user1.getRole().getId())))
                .andExpect(jsonPath("$[0].responsible.role.name", is(user1.getRole().getName().toString())))
                .andExpect(jsonPath("$[0].responsible.accountNonExpired", is(true)))
                .andExpect(jsonPath("$[0].responsible.accountNonLocked", is(true)))
                .andExpect(jsonPath("$[0].responsible.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$[0].responsible.enabled", is(true)))
                .andExpect(jsonPath("$[1].id", is(responseEventDto2.getId())))
                .andExpect(jsonPath("$[1].name", is(responseEventDto2.getName())))
                .andExpect(jsonPath("$[1].description", is(responseEventDto2.getDescription())))
                .andExpect(jsonPath("$[1].startTime", is(responseEventDto2.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$[1].endTime", is(responseEventDto2.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$[1].responsible.id", is(user2.getId())))
                .andExpect(jsonPath("$[1].responsible.email", is(user2.getEmail())))
                .andExpect(jsonPath("$[1].responsible.password", is(user2.getPassword())))
                .andExpect(jsonPath("$[1].responsible.role.id", is(user2.getRole().getId())))
                .andExpect(jsonPath("$[1].responsible.role.name", is(user2.getRole().getName().toString())))
                .andExpect(jsonPath("$[1].responsible.accountNonExpired", is(false)))
                .andExpect(jsonPath("$[1].responsible.accountNonLocked", is(false)))
                .andExpect(jsonPath("$[1].responsible.credentialsNonExpired", is(false)))
                .andExpect(jsonPath("$[1].responsible.enabled", is(false)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void patchEventForWork() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        User user = new User(1, "vitekb651@gmail.com", "123456789", role, true, true, true, true);

        LocalDateTime currentTime = LocalDateTime.now();

        PatchEventDto patchEventDto = new PatchEventDto("Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user.getId());
        ResponseEventDto responseEventDto = new ResponseEventDto(1, "Adeptus Astartes angels of Emperor", "In the light i stay, in the light i pray", currentTime.plusDays(1), currentTime.plusDays(2), user);

        when(userEventService.patchEventForWork(Mockito.any(PatchEventDto.class), Mockito.anyInt()))
                .thenReturn(responseEventDto);

        mvc.perform(patch("/user/event/{eventId}", 1)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(patchEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseEventDto.getId())))
                .andExpect(jsonPath("$.name", is(responseEventDto.getName())))
                .andExpect(jsonPath("$.description", is(responseEventDto.getDescription())))
                .andExpect(jsonPath("$.startTime", is(responseEventDto.getStartTime().format(FORMATTER))))
                .andExpect(jsonPath("$.endTime", is(responseEventDto.getEndTime().format(FORMATTER))))
                .andExpect(jsonPath("$.responsible.id", is(user.getId())))
                .andExpect(jsonPath("$.responsible.email", is(user.getEmail())))
                .andExpect(jsonPath("$.responsible.password", is(user.getPassword())))
                .andExpect(jsonPath("$.responsible.role.id", is(user.getRole().getId())))
                .andExpect(jsonPath("$.responsible.role.name", is(user.getRole().getName().toString())))
                .andExpect(jsonPath("$.responsible.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.responsible.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.responsible.enabled", is(true)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteEventForWork() throws Exception {

        mvc.perform(delete("/user/event/{eventId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(userEventService, Mockito.times(1))
                .deleteEventForWork(1);
    }
}