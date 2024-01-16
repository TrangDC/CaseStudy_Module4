package com.example.case_study_m4.repository;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findOrderDetailsByOrder(Order order);
}
