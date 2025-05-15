package com.beatstore.BeatStore.unit.services;

import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.shared.Role;
import com.beatstore.BeatStore.user.model.User;
import com.beatstore.BeatStore.beat.repository.BeatRepository;
import com.beatstore.BeatStore.beat.service.BeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeatServiceTest {
    @Mock
    private BeatRepository beatRepository;

    @InjectMocks
    private BeatService beatService;

    User sellerUser = new User();


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        sellerUser.setUsername("testuser");
        sellerUser.setEmail("test@example.com");
        sellerUser.setPassword("plainPassword");
        sellerUser.setRole(Role.SELLER);
    }



    @Test
    void createBeat(){



        Beat rawBeat = new Beat();
        rawBeat.setTitle("BeatTitle");
        rawBeat.setBpm(120);
        rawBeat.setGenre("D&B");
        rawBeat.setPrice(14.99);
        rawBeat.setDownloadUrl("https://download.com");
        rawBeat.setSeller(sellerUser);
        rawBeat.setCreatedAt(LocalDateTime.now());


        when(beatRepository.save(Mockito.any(Beat.class))).thenAnswer(i -> i.getArgument(0));

        Beat savedBeat = beatService.createBeat(rawBeat);

        assertNotNull(savedBeat);
        verify(beatRepository).save(any(Beat.class));

    }

    @Test
    void getAllBeats(){
        List<Beat> beats = Arrays.asList(
                new Beat(1L,"Title", "EDM", 120, 12.99, "https://download.com", LocalDateTime.now(),sellerUser),
                new Beat(2L,"Title2", "D&B", 120, 12.99, "https://download.com", LocalDateTime.now(),sellerUser)
                );

        when(beatRepository.findAll()).thenReturn(beats);

        List<Beat> result = beatService.getAllBeats();

        assertEquals(2, result.size());
        verify(beatRepository, times(1)).findAll();

    }


    @Test
    void getBeatById(){
        Beat beat = new Beat(1L,"Title", "EDM", 120, 12.99, "https://download.com", LocalDateTime.now(),sellerUser);

        when(beatRepository.findById(1L)).thenReturn(Optional.of(beat));

        Optional<Beat> result = beatService.getBeatById(1L);

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());


    }
}
