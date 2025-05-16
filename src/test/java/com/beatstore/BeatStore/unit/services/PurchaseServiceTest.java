package com.beatstore.BeatStore.unit.services;
import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.purchase.model.Purchase;
import com.beatstore.BeatStore.user.model.User;
import com.beatstore.BeatStore.beat.repository.BeatRepository;
import com.beatstore.BeatStore.purchase.repository.PurchaseRepository;
import com.beatstore.BeatStore.user.repository.UserRepository;
import com.beatstore.BeatStore.purchase.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.persistence.EntityNotFoundException;

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

    @Test
    void shouldReturnPurchasesByUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(purchaseRepository.findByBuyerId(userId)).thenReturn(Collections.emptyList());

        List<Purchase> result = purchaseService.getPurchasesByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(purchaseRepository).findByBuyerId(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> purchaseService.getPurchasesByUser(userId));

    }
}