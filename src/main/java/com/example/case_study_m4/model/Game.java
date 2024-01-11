package com.example.case_study_m4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "games")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    private String name;

    @NotEmpty
    @NotBlank
    private String description;
    private Long price;
    private Long quantity;
    private String releaseDate;
    private String platform;
    private Long rating;
    private boolean isActive;
    private String image;
    private String imageDetail;

    @ManyToOne
    @JoinColumn(name = "c_id")
    private Category category;
}
