package com.beatstore.BeatStore.integration.controller;


import com.beatstore.BeatStore.models.Beat;
import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.BeatRepository;
import com.beatstore.BeatStore.repositories.PurchaseRepository;
import com.beatstore.BeatStore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
 class BeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BeatRepository beatRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();  // Purchase depende de Beat y User
        beatRepository.deleteAll();      // Beat depende de User
        userRepository.deleteAll();
    }


    @Test
    void shouldCreateBeat() throws Exception{

        String user = """
                {
                    "username": "integrationuser",
                    "email": "integration@example.com",
                    "password": "secure123"
                    
                }
                """;
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user))
                .andExpect(status().isCreated());

        String beatJson = """
        {
              "title": "Energetic Trap Beat",
              "genre": "Trap",
              "bpm": 140,
              "price": 19.99,
              "downloadUrl": "https://example.com/beats/trap1",
              "seller": {
                "id": 1
              }
            }
        """;

        mockMvc.perform(post("/api/beats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beatJson))
                .andExpect(status().isCreated());

        // Recuperar el usuario de la base de datos
        Beat beat = beatRepository.findByTitle("Energetic Trap Beat").orElse(null);
        assertThat(beat).isNotNull();
        assertThat(beat.getGenre()).isEqualTo("Trap");

    }


}
