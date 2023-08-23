package org.example.repository;

import org.example.entity.BasketItemEntity;
import org.example.entity.FavoriteProductEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItemEntity, Integer> {
    @Override
    <S extends BasketItemEntity> S save(S entity);

    @Override
    Optional<BasketItemEntity> findById(Integer integer);

    @Override
    long count();

    @Override
    void delete(BasketItemEntity entity);

    List<BasketItemEntity> findByUserEntity(UserEntity user);

    Optional<BasketItemEntity> findByUserEntityAndProductEntity(UserEntity user, ProductEntity productEntity);
}
