package com.beatstore.BeatStore.integration.controller;

import com.beatstore.BeatStore.beat.model.Beat;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeatRepository beatRepository;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();  // Purchase depende de Beat y User
        beatRepository.deleteAll();      // Beat depende de User
        userRepository.deleteAll();
    }


    @Test
    void shouldCreatePurchase() throws Exception {

        // Crear usuario comprador
        String buyerJson = """
            {
              "username": "testbuyer",
              "email": "buyer@example.com",
              "password": "password123"
            }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buyerJson))
                .andExpect(status().isCreated());

        User savedUser = userRepository.findByUsername("testbuyer").orElseThrow();
        Long buyerId = savedUser.getId();

        // Crear beat
        String beatJson = """
            {
              "title": "Chill Beat",
              "genre": "Lo-Fi",
              "bpm": 80,
              "price": 14.99,
              "downloadUrl": "https://example.com/beat",
              "seller": { "id": %d }
            }
        """.formatted(buyerId);

        mockMvc.perform(post("/api/beats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beatJson))
                .andExpect(status().isCreated());


        Beat savedBeat = beatRepository.findByTitle("Chill Beat").orElseThrow();
        Long beatId = savedBeat.getId();

        System.out.println( buyerId + " "  + beatId);

        String purchaseJson = """
        {
          "buyerId": %d,
          "beatId": %d
        }
    """.formatted(buyerId, beatId);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson))
                .andExpect(status().isCreated());

        assertThat(purchaseRepository.findAll()).hasSize(1);


    }

    @Test
    void shouldReturnEmptyPurchasesForNewUser() throws Exception {
        // Crear un usuario de prueba
        String userJson = """
        {
            "username": "buyer1",
            "email": "buyer1@example.com",
            "password": "test123"
        }
    """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Llamar al endpoint de compras por usuario
        mockMvc.perform(get("/api/purchases/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}

