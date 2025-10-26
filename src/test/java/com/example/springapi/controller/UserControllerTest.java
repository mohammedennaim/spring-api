package com.example.springapi.controller;

import com.example.springapi.model.User;
import com.example.springapi.service.IUserService;
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
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private IUserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController controller = new UserController(userService);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(exceptionHandler)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        // Given
        User user1 = new User("John Doe", "john@example.com");
        user1.setId(1L);
        User user2 = new User("Jane Doe", "jane@example.com");
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Utilisateurs récupérés avec succès"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].email").value("john@example.com"));
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        // Given
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        when(userService.getById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Utilisateur récupéré avec succès"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.fullName").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        // Given
        when(userService.getById(999L)).thenThrow(new NoSuchElementException("Utilisateur non trouvé avec l'ID: 999"));

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Utilisateur non trouvé"))
                .andExpect(jsonPath("$.error").value("Aucun utilisateur trouvé avec l'ID: 999"));
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        // Given
        User user = new User("John Doe", "john@example.com");
        User savedUser = new User("John Doe", "john@example.com");
        savedUser.setId(1L);

        when(userService.save(any(User.class))).thenReturn(savedUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Utilisateur créé avec succès"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.fullName").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    public void testCreateUser_InvalidData() throws Exception {
        // Given
        User user = new User("", "john@example.com"); // Nom vide

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Données invalides"))
                .andExpect(jsonPath("$.error").value("Le nom est obligatoire"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        // Given
        User user = new User("John Updated", "john.updated@example.com");
        user.setId(1L);
        when(userService.getById(1L)).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Utilisateur mis à jour avec succès"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.fullName").value("John Updated"))
                .andExpect(jsonPath("$.data.email").value("john.updated@example.com"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        // Given
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        when(userService.getById(1L)).thenReturn(user);
        doNothing().when(userService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Utilisateur supprimé avec succès"));
    }
}