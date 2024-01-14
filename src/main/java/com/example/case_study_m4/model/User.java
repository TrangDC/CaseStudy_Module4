package com.example.case_study_m4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    private Long balance;
    private String address;
    private String username;
    private String password;
    private String email;
    private boolean isActive;


    public User(String name, String gender, String address, String username, String password, String email) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.balance = 0L;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = true;
    }
}
