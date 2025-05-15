package com.beatstore.BeatStore.integration.controller;

import com.beatstore.BeatStore.user.model.User;
import com.beatstore.BeatStore.beat.repository.BeatRepository;
import com.beatstore.BeatStore.purchase.repository.PurchaseRepository;
import com.beatstore.BeatStore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
@AutoConfigureMockMvc(addFilters = false)
  class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeatRepository beatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();  // Purchase depende de Beat y User
        beatRepository.deleteAll();      // Beat depende de User
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserWithHashedPassword() throws Exception{

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
