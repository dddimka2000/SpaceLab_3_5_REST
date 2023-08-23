package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "favorite_product")
public class FavoriteProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    @Column(name = "status")
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;
}
