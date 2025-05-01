package com.beatstore.BeatStore.unit.services;
import com.beatstore.BeatStore.models.Beat;
import com.beatstore.BeatStore.models.Purchase;
import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.BeatRepository;
import com.beatstore.BeatStore.repositories.PurchaseRepository;
import com.beatstore.BeatStore.repositories.UserRepository;
import com.beatstore.BeatStore.services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BeatRepository beatRepository;

    @InjectMocks
    private PurchaseService purchaseService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePurchaseSuccessfully() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setUsername("buyer1");

        Beat beat = new Beat();
        beat.setId(1L);
        beat.setTitle("Test Beat");
        beat.setPrice(19.99);

        Purchase purchase = Purchase.builder()
                .buyer(buyer)
                .beat(beat)
                .pricePaid(beat.getPrice())
                .purchaseDate(LocalDateTime.now())
                .build();

        when(purchaseRepository.save(any(Purchase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(beatRepository.findById(1L)).thenReturn(Optional.of(beat));

        Purchase savedPurchase = purchaseService.createPurchase(buyer.getId(), beat.getId());

        assertNotNull(savedPurchase);
        assertEquals(19.99, savedPurchase.getPricePaid());
        assertEquals("Test Beat", savedPurchase.getBeat().getTitle());

        verify(purchaseRepository).save(any(Purchase.class));
    }
}