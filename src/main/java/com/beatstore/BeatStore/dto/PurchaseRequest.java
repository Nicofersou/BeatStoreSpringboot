package com.beatstore.BeatStore.dto;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long buyerId;
    private Long beatId;
}