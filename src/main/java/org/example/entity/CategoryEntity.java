package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "category")
public class CategoryEntity {

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
    @Column
    public String description;

    @ManyToOne
    @JoinColumn(name = "id_classification")
    private ClassificationEntity classificationEntity;

    @OneToMany(fetch = FetchType.EAGER)
    List<ProductEntity> productEntityList;


}

