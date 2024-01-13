package com.example.case_study_m4.repository;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameUpRepository {
    Page<Game> findAllByNameContaining(String word, Pageable pageable);
    public Iterable<Game> findByNameContaining(String word);
    Page<Game> findAllByCategory(Category category, Pageable pageable);
}
