package com.beatstore.BeatStore.repositories;

import com.beatstore.BeatStore.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBuyerId(Long buyerId);
    List<Purchase> findByBeatId(Long beatId);
}
