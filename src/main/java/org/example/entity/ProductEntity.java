package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.CategoryDTO;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "product")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "path")
    private String path;

    @Column(name = "status")
    private Boolean status;
    @Column(name = "description")
    public String description;
    @Column(name = "price")
    public BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private CategoryEntity categoryEntity;
    @OneToMany
    List<ProductByOrderEntity> productByOrderEntities;
    @OneToMany
    List<BasketItemEntity> basketItemEntities;
}

