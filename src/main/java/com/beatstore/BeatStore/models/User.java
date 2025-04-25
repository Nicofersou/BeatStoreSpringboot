package com.beatstore.BeatStore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size (min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}
