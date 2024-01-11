package com.example.case_study_m4.service;

import com.example.case_study_m4.model.User;

public interface IUserService extends IGenerateService<User>  {
    void save(User user);
}
