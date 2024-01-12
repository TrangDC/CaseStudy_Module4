package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.User;
import com.example.case_study_m4.service.IUserService;

import java.util.Optional;

public class UserService implements IUserService {
    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void remove(Long id) {

    }
}
