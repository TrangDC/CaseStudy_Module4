package com.example.case_study_m4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Timestamp orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;

    @Column(name = "payment_method", nullable = false)
    private String payment_method;

    @Column(name = "status", nullable = false)
    private String status;

}
