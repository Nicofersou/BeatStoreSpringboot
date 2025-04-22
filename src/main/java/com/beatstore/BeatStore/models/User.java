package com.beatstore.BeatStore.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data               // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor vacío (necesario para JPA)
@AllArgsConstructor // Constructor con todos los atributos
@Builder            // Permite usar el patrón builder (opcional pero útil)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
}
