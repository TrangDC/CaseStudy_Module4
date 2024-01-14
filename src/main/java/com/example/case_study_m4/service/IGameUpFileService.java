package com.example.case_study_m4.service;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.GameUpFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IGameUpFileService {
    Page<GameUpFile> findAll(Pageable pageable);

    Optional<GameUpFile> findById(Long id);

    void save(GameUpFile gameUpFile);

    void remove(Long id);
    public Iterable<GameUpFile> searchByWord(String word);

    public Page<GameUpFile> searchByWord(String word, Pageable pageable);

    Page<GameUpFile> findByCategory(Category category, Pageable pageable);
}
