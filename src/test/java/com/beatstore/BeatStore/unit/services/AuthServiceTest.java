package com.beatstore.BeatStore.unit.services;

import com.beatstore.BeatStore.auth.AuthService;
import com.beatstore.BeatStore.auth.dto.AuthenticationRequest;
import com.beatstore.BeatStore.auth.dto.AuthenticationResponse;
import com.beatstore.BeatStore.auth.dto.RegisterRequest;
import com.beatstore.BeatStore.auth.security.JwtService;
import com.beatstore.BeatStore.shared.Role;
import com.beatstore.BeatStore.user.model.User;
import com.beatstore.BeatStore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void register_ShouldReturnToken() {
        RegisterRequest request = new RegisterRequest("testuser", "test@email.com", "password", Role.BUYER);

        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");

        User user = User.builder()
                .username("testuser")
                .email("test@email.com")
                .password("hashedpassword")
                .role(Role.BUYER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        AuthenticationResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
    }

    @Test
    void authenticate_ShouldReturnToken() {
        AuthenticationRequest request = new AuthenticationRequest("testuser", "password");

        User user = User.builder()
                .username("testuser")
                .email("test@email.com")
                .password("hashedpassword")
                .role(Role.BUYER)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        AuthenticationResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password")
        );
    }
}