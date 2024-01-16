package com.example.case_study_m4.service.impl;

import com.example.case_study_m4.model.Order;
import com.example.case_study_m4.model.User;
import com.example.case_study_m4.repository.IOrderRepository;
import com.example.case_study_m4.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository iOrderRepository;
    @Override
    public Iterable<Order> findAll() {
        return iOrderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return iOrderRepository.findById(id);
    }

    @Override
    public void save(Order order) {
        iOrderRepository.save(order);
    }

    @Override
    public void remove(Long id) {

    }


    @Override
    public List<Order> findByUserId(Long userId) {
        return iOrderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersWithDetailsByUserId(Long userId) {
        return iOrderRepository.findByUserId(userId);
    }
}
