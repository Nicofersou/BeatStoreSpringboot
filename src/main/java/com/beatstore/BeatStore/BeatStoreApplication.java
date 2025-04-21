package com.beatstore.BeatStore;

import com.beatstore.BeatStore.models.User;
import com.beatstore.BeatStore.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeatStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeatStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			// Insertamos un usuario de prueba para forzar la creación de la tabla
			User user = new User("testuser", "test@example.com");
			repository.save(user);
			System.out.println("Usuario insertado.");
		};
	}
}
