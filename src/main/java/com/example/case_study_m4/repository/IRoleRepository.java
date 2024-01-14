package com.example.case_study_m4.repository;

import com.example.case_study_m4.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
