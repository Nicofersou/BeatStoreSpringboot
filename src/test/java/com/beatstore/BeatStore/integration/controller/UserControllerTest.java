package com.beatstore.BeatStore.integration.controller;

import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
  class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUserWithHashedPassword() throws Exception{
        userRepository.deleteAll();

        String jsonRequest = """
                {
                    "username": "integrationuser",
                    "email": "integration@example.com",
                    "password": "secure123"
                }
                """;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        // Recuperar el usuario de la base de datos
        User user = userRepository.findByUsername("integrationuser").orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("integration@example.com");

        // Verificar que la contraseña está hasheada
        assertThat(user.getPassword()).isNotEqualTo("secure123");
        assertThat(passwordEncoder.matches("secure123", user.getPassword())).isTrue();
    }


}
