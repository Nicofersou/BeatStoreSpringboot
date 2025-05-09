package com.beatstore.BeatStore.controllers;


import com.beatstore.BeatStore.dto.PurchaseRequest;
import com.beatstore.BeatStore.models.Purchase;
import com.beatstore.BeatStore.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequest request) {
        Purchase purchase = purchaseService.createPurchase(request.getBuyerId(), request.getBeatId());
        return ResponseEntity.status(201).body(purchase);
    }

}
