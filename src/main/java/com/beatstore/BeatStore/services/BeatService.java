package com.beatstore.BeatStore.services;

import com.beatstore.BeatStore.models.Beat;
import com.beatstore.BeatStore.repositories.BeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeatService {
    private final BeatRepository beatRepository;

    @Autowired
    public BeatService(BeatRepository beatRepository){
        this.beatRepository = beatRepository;
    }

    public List<Beat> getAllBeats(){return beatRepository.findAll();}

    public Optional<Beat> getBeatById(Long id){return beatRepository.findById(id);}

    public Beat createBeat (Beat beat){return beatRepository.save(beat);}

    public void deleteBeat(Long id){beatRepository.deleteById(id);}

}
