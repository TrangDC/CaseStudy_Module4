package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.User;
import com.example.case_study_m4.repository.IUserRepository;
import com.example.case_study_m4.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByID(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findAllPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Boolean checkPasswordUser(String email, String password) {
        User checkUser = userRepository.findUserByEmail(email);
        if (checkUser.getPassword().equals(password)) return true;
        return false;
    }

    @Override
    public Boolean checkUserByEmail(String email) {
        User checkUser = userRepository.findUserByEmail(email);
        if(checkUser==null) return false;
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public void getUserByBalance(Long balance) {
        userRepository.getUserByBalance(balance);
    }

}
