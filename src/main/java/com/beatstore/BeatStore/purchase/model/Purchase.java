package com.beatstore.BeatStore.purchase.model;

import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "beat_id")
    private Beat beat;

    private LocalDateTime purchaseDate = LocalDateTime.now();

    private Double pricePaid;
}
