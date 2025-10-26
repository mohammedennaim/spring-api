package com.example.springapi.service.impl;

import com.example.springapi.model.User;
import com.example.springapi.repository.IUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private IUserRepository repository;

    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(repository);
    }

    @Test
    public void testGetAll_Success() {
        // Given
        User user1 = new User("John Doe", "john@example.com");
        user1.setId(1L);
        User user2 = new User("Jane Doe", "jane@example.com");
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);

        when(repository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("Jane Doe", result.get(1).getFullName());
        verify(repository).findAll();
    }

    @Test
    public void testGetById_Success() {
        // Given
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        verify(repository).findById(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetById_NullId() {
        // When
        userService.getById(null);

        // Then - Exception expected
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetById_NotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When
        userService.getById(999L);

        // Then - Exception expected
    }

    @Test
    public void testSave_NewUser_Success() {
        // Given
        User user = new User("John Doe", "john@example.com");
        User savedUser = new User("John Doe", "john@example.com");
        savedUser.setId(1L);

        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.save(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        verify(repository).existsByEmail("john@example.com");
        verify(repository).save(user);
    }

    @Test
    public void testSave_UpdateUser_Success() {
        // Given
        User user = new User("John Updated", "john.updated@example.com");
        user.setId(1L);
        User existingUser = new User("John Old", "john.old@example.com");
        existingUser.setId(1L);

        when(repository.findByEmail("john.updated@example.com")).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.save(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("John Updated", result.getFullName());
        verify(repository).findByEmail("john.updated@example.com");
        verify(repository).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSave_NullUser() {
        // When
        userService.save(null);

        // Then - Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSave_EmptyFullName() {
        // Given
        User user = new User("", "john@example.com");

        // When
        userService.save(user);

        // Then - Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSave_EmptyEmail() {
        // Given
        User user = new User("John Doe", "");

        // When
        userService.save(user);

        // Then - Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSave_DuplicateEmail() {
        // Given
        User user = new User("John Doe", "john@example.com");
        when(repository.existsByEmail("john@example.com")).thenReturn(true);

        // When
        userService.save(user);

        // Then - Exception expected
    }

    @Test
    public void testDelete_Success() {
        // Given
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        userService.delete(1L);

        // Then
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NullId() {
        // When
        userService.delete(null);

        // Then - Exception expected
    }

    @Test(expected = NoSuchElementException.class)
    public void testDelete_NotFound() {
        // Given
        when(repository.existsById(999L)).thenReturn(false);

        // When
        userService.delete(999L);

        // Then - Exception expected
    }
}
