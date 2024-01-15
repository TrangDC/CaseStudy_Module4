package com.example.case_study_m4.repository;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.OrderDetail;
import com.example.case_study_m4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
