package com.beatstore.BeatStore.purchase.dto;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long buyerId;
    private Long beatId;
}