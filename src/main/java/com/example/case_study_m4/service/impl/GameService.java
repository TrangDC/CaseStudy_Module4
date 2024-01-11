package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.Game;
import com.example.case_study_m4.repository.IGameRepository;
import com.example.case_study_m4.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService implements IGameService {
    @Autowired
    private IGameRepository iGameRepository;

    @Override
    public Page<Game> findAll(Pageable pageable) {
        return iGameRepository.findAll(pageable);
    }

    @Override
    public Optional<Game> findById(Long id) {
        return iGameRepository.findById(id);
    }

    @Override
    public void save(Game game) {
        iGameRepository.save(game);
    }

    @Override
    public void remove(Long id) {
        iGameRepository.deleteById(id);
    }

    @Override
    public Iterable<Game> searchByWord(String word) {
        return iGameRepository.findByNameContaining(word);
    }

    @Override
    public Page<Game> searchByWord(String word, Pageable pageable) {
        return iGameRepository.findAllByNameContaining(word,pageable);
    }
}
