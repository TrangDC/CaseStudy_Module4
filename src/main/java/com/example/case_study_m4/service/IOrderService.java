package com.example.case_study_m4.service;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.OrderDetail;
import com.example.case_study_m4.model.User;
import com.example.case_study_m4.repository.IOrderDetailRepository;
import com.example.case_study_m4.repository.IOrderRepository;

import java.util.List;

public interface IOrderService extends IGenerateService<Order> {
    List<Order> findByUserId(Long userId);
}
