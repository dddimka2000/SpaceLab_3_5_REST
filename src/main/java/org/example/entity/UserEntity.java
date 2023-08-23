package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user", schema = "products_schema")
@NoArgsConstructor
@Data
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "login")
    public String login;

    @Column(name = "pass")
    private String pass;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "path")
    private String path;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "userEntity")
    List<OrderTableEntity> orderTableEntities;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "userEntity")
    List<BasketItemEntity> basketItemEntities;
}
