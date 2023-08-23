package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "order_table")
public class OrderTableEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "date_time")
    LocalDateTime dateTime;
    @Column
    String comment;
    @OneToMany
    List<ProductByOrderEntity> productByOrderEntity;

}
