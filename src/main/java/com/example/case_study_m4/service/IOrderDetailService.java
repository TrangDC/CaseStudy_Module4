package com.example.case_study_m4.service;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.OrderDetail;
import com.example.case_study_m4.repository.IOrderDetailRepository;

import java.util.List;

public interface IOrderDetailService extends IGenerateService<OrderDetail> {
    List<OrderDetail> findByOrder(Order order);
}
