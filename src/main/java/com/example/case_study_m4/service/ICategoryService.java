package com.example.case_study_m4.service;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoryService {
    public Iterable<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    public void save(Category category);
    Optional<Category> findById(Long id);
    public void remove(Long id);
}
