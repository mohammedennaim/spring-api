package com.example.springapi.controller;

import com.example.springapi.model.User;
import com.example.springapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setFullName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFullName("Jane Doe");
        user2.setEmail("jane@example.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");

        when(userService.getById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Given
        User user = new User();
        user.setFullName("John Doe");
        user.setEmail("john@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("John Doe");
        savedUser.setEmail("john@example.com");

        when(userService.save(any(User.class))).thenReturn(savedUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe Updated");
        user.setEmail("john.updated@example.com");

        when(userService.save(any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Given
        // Mock the delete method (void method)
        doNothing().when(userService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
