package org.example.repository;

import org.example.entity.FavoriteProductEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProductEntity,Integer> {

    @Override
    <S extends FavoriteProductEntity> S save(S entity);

    @Override
    Optional<FavoriteProductEntity> findById(Integer integer);

    @Override
    long count();

    @Override
    void delete(FavoriteProductEntity entity);

    List<FavoriteProductEntity> findByUserEntity(UserEntity user);

    Optional<FavoriteProductEntity> findByUserEntityAndProductEntity(UserEntity user, ProductEntity productEntity);

}
