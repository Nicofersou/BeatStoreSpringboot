package com.beatstore.BeatStore.services;

import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testCreateUser() {
        User user = new User(null, "juan", "juan@email.com");

        when(userRepository.save(user)).thenReturn(new User(1L, "juan", "juan@email.com"));

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser.getId());
        assertEquals("juan", savedUser.getUsername());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "ana", "ana@email.com"),
                new User(2L, "pedro", "pedro@email.com")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "carlos", "carlos@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("carlos", result.get().getUsername());
    }
}
