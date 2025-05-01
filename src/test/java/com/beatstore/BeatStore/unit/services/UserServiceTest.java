package com.beatstore.BeatStore.unit.services;

import com.beatstore.BeatStore.models.Role;
import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.UserRepository;
import com.beatstore.BeatStore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void createUser_shouldHashPasswordBeforeSaving() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        User rawUser = new User();
        rawUser.setUsername("testuser");
        rawUser.setEmail("test@example.com");
        rawUser.setPassword("plainPassword");
        rawUser.setRole(Role.BUYER);

        // Supongamos que este es el hash resultante
        String hashedPassword = "$2a$10$ABC123";
        when(passwordEncoder.encode("plainPassword")).thenReturn(hashedPassword);

        // El userRepository simula devolver el mismo usuario que recibe
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        User savedUser = userService.createUser(rawUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals(hashedPassword, savedUser.getPassword());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "ana", "ana@email.com", "abc123abc", Role.BUYER),
                new User(2L, "pedro", "pedro@email.com", "abc123abc", Role.BUYER)
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "carlos", "carlos@email.com", "abc123abc",Role.BUYER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("carlos", result.get().getUsername());
    }
}
