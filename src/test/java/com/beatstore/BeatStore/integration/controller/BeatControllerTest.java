package com.beatstore.BeatStore.integration.controller;


import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.beat.repository.BeatRepository;
import com.beatstore.BeatStore.purchase.repository.PurchaseRepository;
import com.beatstore.BeatStore.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
 class BeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BeatRepository beatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();  // Purchase depende de Beat y User
        beatRepository.deleteAll();      // Beat depende de User
        userRepository.deleteAll();
    }


    @Test
    void shouldCreateBeat() throws Exception {
        String userJson = """
                {
                    "username": "integrationuser",
                    "email": "integration@example.com",
                    "password": "secure123"
                }
                """;

        // Crear usuario y obtener respuesta
        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parsear la respuesta para obtener el ID del usuario creado
        JsonNode userNode = objectMapper.readTree(userResponse);
        Long userId = userNode.get("id").asLong();

        // Construir JSON del Beat con el ID correcto
        String beatJson = String.format("""
                {
                    "title": "Energetic Trap Beat",
                    "genre": "Trap",
                    "bpm": 140,
                    "price": 19.99,
                    "downloadUrl": "https://example.com/beats/trap1",
                    "seller": {
                        "id": %d
                    }
                }
                """, userId);

        mockMvc.perform(post("/api/beats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beatJson))
                .andExpect(status().isCreated());

        // Validaciones
        Beat beat = beatRepository.findByTitle("Energetic Trap Beat").orElse(null);
        assertThat(beat).isNotNull();
        assertThat(beat.getGenre()).isEqualTo("Trap");
    }
}