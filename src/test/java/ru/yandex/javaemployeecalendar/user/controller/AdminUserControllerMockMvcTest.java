package ru.yandex.javaemployeecalendar.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.javaemployeecalendar.user.dto.PatchUserDto;
import ru.yandex.javaemployeecalendar.user.dto.PostUserDto;
import ru.yandex.javaemployeecalendar.user.dto.ResponseUserDto;
import ru.yandex.javaemployeecalendar.user.model.Role;
import ru.yandex.javaemployeecalendar.user.model.RoleEnum;
import ru.yandex.javaemployeecalendar.user.service.AdminUserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminUserController.class)
class AdminUserControllerMockMvcTest {

    @MockBean
    AdminUserService adminUserService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postUser() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "123456789", role.getId());
        ResponseUserDto responseUserDto = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        when(adminUserService.postUser(Mockito.any(PostUserDto.class)))
                .thenReturn(responseUserDto);

        mvc.perform(post("/admin/users")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseUserDto.getId())))
                .andExpect(jsonPath("$.email", is(responseUserDto.getEmail())))
                .andExpect(jsonPath("$.password", is(responseUserDto.getPassword())))
                .andExpect(jsonPath("$.role.id", is(role.getId())))
                .andExpect(jsonPath("$.role.name", is(role.getName().toString())))
                .andExpect(jsonPath("$.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postUserValidationEmailException() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("1", "123456789", role.getId());

        mvc.perform(post("/admin/users")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postUserValidationPasswordException() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "1", role.getId());

        mvc.perform(post("/admin/users")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postUserThrows401() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        PostUserDto postUserDto = new PostUserDto("vitekb652@gmail.com", "123456789", role.getId());
        ResponseUserDto responseUserDto = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        when(adminUserService.postUser(Mockito.any(PostUserDto.class)))
                .thenReturn(responseUserDto);

        mvc.perform(post("/admin/users")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserById() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        ResponseUserDto responseUserDto = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        when(adminUserService.getUserById(Mockito.anyInt()))
                .thenReturn(responseUserDto);

        mvc.perform(get("/admin/users/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseUserDto.getId())))
                .andExpect(jsonPath("$.email", is(responseUserDto.getEmail())))
                .andExpect(jsonPath("$.password", is(responseUserDto.getPassword())))
                .andExpect(jsonPath("$.role.id", is(role.getId())))
                .andExpect(jsonPath("$.role.name", is(role.getName().toString())))
                .andExpect(jsonPath("$.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.enabled", is(true)));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserWorkingTimeById() throws Exception {

        when(adminUserService.getUserWorkingTimeByUserId(Mockito.anyInt()))
                .thenReturn(16);

        MvcResult result = mvc.perform(get("/admin/users/time/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals(content, "16");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsers() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        ResponseUserDto responseUserDto1 = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);
        ResponseUserDto responseUserDto2 = new ResponseUserDto(2, "vitekb653@gmail.com", "12345678910", role, true, true, true, true);

        when(adminUserService.getAllUsers(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(responseUserDto1, responseUserDto2));

        mvc.perform(get("/admin/users")
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseUserDto1.getId())))
                .andExpect(jsonPath("$[0].email", is(responseUserDto1.getEmail())))
                .andExpect(jsonPath("$[0].password", is(responseUserDto1.getPassword())))
                .andExpect(jsonPath("$[0].role.id", is(role.getId())))
                .andExpect(jsonPath("$[0].role.name", is(role.getName().toString())))
                .andExpect(jsonPath("$[0].accountNonExpired", is(true)))
                .andExpect(jsonPath("$[0].accountNonLocked", is(true)))
                .andExpect(jsonPath("$[0].credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$[0].enabled", is(true)))
                .andExpect(jsonPath("$[1].id", is(responseUserDto2.getId())))
                .andExpect(jsonPath("$[1].email", is(responseUserDto2.getEmail())))
                .andExpect(jsonPath("$[1].password", is(responseUserDto2.getPassword())))
                .andExpect(jsonPath("$[1].role.id", is(role.getId())))
                .andExpect(jsonPath("$[1].role.name", is(role.getName().toString())))
                .andExpect(jsonPath("$[1].accountNonExpired", is(true)))
                .andExpect(jsonPath("$[1].accountNonLocked", is(true)))
                .andExpect(jsonPath("$[1].credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$[1].enabled", is(true)));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void patchUser() throws Exception {

        Role role = new Role(1, RoleEnum.ADMIN);
        PatchUserDto patchUserDto = new PatchUserDto("vitekb652@gmail.com", "123456789", 2, true, true, true, true);
        ResponseUserDto responseUserDto = new ResponseUserDto(1, "vitekb652@gmail.com", "123456789", role, true, true, true, true);

        when(adminUserService.patchUser(Mockito.any(PatchUserDto.class), Mockito.anyInt()))
                .thenReturn(responseUserDto);

        mvc.perform(patch("/admin/users/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(patchUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseUserDto.getId())))
                .andExpect(jsonPath("$.email", is(responseUserDto.getEmail())))
                .andExpect(jsonPath("$.password", is(responseUserDto.getPassword())))
                .andExpect(jsonPath("$.role.id", is(role.getId())))
                .andExpect(jsonPath("$.role.name", is(role.getName().toString())))
                .andExpect(jsonPath("$.accountNonExpired", is(true)))
                .andExpect(jsonPath("$.accountNonLocked", is(true)))
                .andExpect(jsonPath("$.credentialsNonExpired", is(true)))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser() throws Exception {

        mvc.perform(delete("/admin/users/{userId}", 1)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(adminUserService, Mockito.times(1))
                .deleteUser(1);

    }
}