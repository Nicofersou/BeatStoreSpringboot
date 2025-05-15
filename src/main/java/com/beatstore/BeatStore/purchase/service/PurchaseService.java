package com.beatstore.BeatStore.purchase.service;


import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.purchase.model.Purchase;
import com.beatstore.BeatStore.user.model.User;
import com.beatstore.BeatStore.beat.repository.BeatRepository;
import com.beatstore.BeatStore.purchase.repository.PurchaseRepository;
import com.beatstore.BeatStore.user.repository.UserRepository;
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
