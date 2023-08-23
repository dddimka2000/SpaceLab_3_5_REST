package org.example.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "product_by_order")
public class ProductByOrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductEntity productEntity;
    @ManyToOne
    @JoinColumn(name = "id_order")
    OrderTableEntity orderTableEntity;
    @Column(name = "count_products")
    private Integer countProducts;


}
