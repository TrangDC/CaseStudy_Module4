package com.example.case_study_m4.repository;

import com.example.case_study_m4.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface IUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String username);
    User getUserByEmail(String email);
    User getUserByBalance(Long balance);
    @Query(
            value = "select * from dbo_users",
            nativeQuery = true)
    List<User> getAllUser();
}
