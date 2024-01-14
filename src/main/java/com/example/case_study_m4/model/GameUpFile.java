package com.example.case_study_m4.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "gameupfiles")
@Data
public class GameUpFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private String imageDetail;
    private Long price;
    private Long quantity;
    private Double rating;
    private String platform;
    private String releaseDate;
    private String description;
    private boolean isActive = true;

    @Transient
    private MultipartFile file;

    @ManyToOne
    @JoinColumn(name = "c_id")
    private Category category;

}
