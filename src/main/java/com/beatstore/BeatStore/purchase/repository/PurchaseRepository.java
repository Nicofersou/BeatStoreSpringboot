package com.beatstore.BeatStore.purchase.repository;

import com.beatstore.BeatStore.purchase.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBuyerId(Long buyerId);
    List<Purchase> findByBeatId(Long beatId);
}
