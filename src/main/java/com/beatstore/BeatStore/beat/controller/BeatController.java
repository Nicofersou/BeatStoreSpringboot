package com.beatstore.BeatStore.beat.controller;


import com.beatstore.BeatStore.beat.model.Beat;
import com.beatstore.BeatStore.beat.service.BeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beats")
public class BeatController {

    private final BeatService beatService;

    @Autowired
    public BeatController(BeatService beatService){
        this.beatService = beatService;
    }


    // Get /api/beats
    @GetMapping
    public List<Beat> getAllBeats(){return beatService.getAllBeats();}

    //GET /api/beats/{id}
    @GetMapping("/{id}")
    public Optional<Beat> getBeatById(@PathVariable Long id){return beatService.getBeatById(id);}

    //POST /api/users
    @PostMapping
    public ResponseEntity<Beat> createBeat(@Valid @RequestBody Beat beat){
        Beat savedBeat = beatService.createBeat(beat);
        return new ResponseEntity<>(savedBeat, HttpStatus.CREATED);
    }


    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public void deleteBeat (@PathVariable Long id){beatService.deleteBeat(id);}


}
