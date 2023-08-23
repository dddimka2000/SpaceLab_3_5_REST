package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "classification")
public class ClassificationEntity {
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

    @OneToMany(fetch = FetchType.LAZY)
    List<CategoryEntity> categoryEntityList;
}
