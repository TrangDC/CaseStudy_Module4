package com.example.case_study_m4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "games")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false)
    private String description;


    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Long price;


    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Max(value = 100, message = "Quantity must be less than or equal to 100")
    private Long quantity;

    @NotBlank(message = "Release Date is required")
    @Column(name = "releaseDate", nullable = false)
    private String releaseDate;

    @NotBlank(message = "Platform is required")
    @Column(name = "platform", nullable = false)
    private String platform;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rating must be greater than or equal to 0.0")
    @DecimalMax(value = "10.0", inclusive = false, message = "Rating must be less than or equal to 10.0")
    private Double rating;

    @ColumnDefault("true")
    private boolean isActive = true;

    private String image;

    private String imageDetail;

    @Transient
    private MultipartFile file;

    @ManyToOne
    @JoinColumn(name = "c_id")
    private Category category;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}