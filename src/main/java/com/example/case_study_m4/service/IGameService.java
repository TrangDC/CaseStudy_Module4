package com.example.case_study_m4.service;

import com.example.case_study_m4.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IGameService {
    Page<Game> findAll(Pageable pageable);

    Optional<Game> findById(Long id);

    void save(Game game);

    void remove(Long id);
    public Iterable<Game> searchByWord(String word);

    public Page<Game> searchByWord(String word, Pageable pageable);
}
