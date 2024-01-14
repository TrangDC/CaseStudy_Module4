package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.GameUpFile;
import com.example.case_study_m4.repository.IGameUpRepository;
import com.example.case_study_m4.service.IGameUpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameUpFileService implements IGameUpFileService {
    @Autowired
    private IGameUpRepository gameUpRepository;
    @Override
    public Page<GameUpFile> findAll(Pageable pageable) {
        return gameUpRepository.findAll(pageable);
    }

    @Override
    public Optional<GameUpFile> findById(Long id) {
        return gameUpRepository.findById(id);
    }

    @Override
    public void save(GameUpFile gameUpFile) {
        gameUpRepository.save(gameUpFile);
    }

    @Override
    public void remove(Long id) {
        gameUpRepository.deleteById(id);
    }

    @Override
    public Iterable<GameUpFile> searchByWord(String word) {
        return null;
    }

    @Override
    public Page<GameUpFile> searchByWord(String word, Pageable pageable) {
        return null;
    }

    @Override
    public Page<GameUpFile> findByCategory(Category category, Pageable pageable) {
        return null;
    }
}
