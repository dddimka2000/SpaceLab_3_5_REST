package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Table(name = "basket_item")
public class BasketItemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductEntity productEntity;
    @Column(name = "count")
    private Integer count;
}
