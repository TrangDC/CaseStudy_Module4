package com.example.case_study_m4.service;

import com.example.case_study_m4.dto.UserDto;
import com.example.case_study_m4.model.User;

import java.util.List;

public interface IUserService extends IGenerateService<User>  {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
