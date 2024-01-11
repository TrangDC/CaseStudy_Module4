package com.example.case_study_m4.service;

import java.util.Optional;

public interface IGenerateService<T> {
    Iterable<T> findAll();

    Optional<T> findByID(Long id);

    void save(T t);

    void remove(Long id);
}
