package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.OrderDetail;
import com.example.case_study_m4.repository.IOrderDetailRepository;
import com.example.case_study_m4.repository.IOrderRepository;
import com.example.case_study_m4.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private IOrderDetailRepository iOrderDetailRepository;
    @Override
    public Iterable<OrderDetail> findAll() {
        return iOrderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return iOrderDetailRepository.findById(id);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        iOrderDetailRepository.save(orderDetail);
    }

    @Override
    public void remove(Long id) {

    }
}
