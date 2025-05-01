package com.beatstore.BeatStore.services;


import com.beatstore.BeatStore.models.Beat;
import com.beatstore.BeatStore.models.Purchase;
import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.BeatRepository;
import com.beatstore.BeatStore.repositories.PurchaseRepository;
import com.beatstore.BeatStore.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BeatRepository beatRepository;
    private final UserRepository userRepository;


    public Purchase createPurchase (Long buyerId, Long beatId){
        User buyer = userRepository.findById(buyerId).orElseThrow(()-> new EntityNotFoundException("Buyer not found"));
        Beat beat = beatRepository.findById(beatId).orElseThrow(() -> new EntityNotFoundException("Beat not found"));

        Purchase purchase = Purchase.builder()
                .buyer(buyer)
                .beat(beat)
                .purchaseDate(LocalDateTime.now())
                .pricePaid(beat.getPrice())
                .build();

        return purchaseRepository.save(purchase);
    }

}
