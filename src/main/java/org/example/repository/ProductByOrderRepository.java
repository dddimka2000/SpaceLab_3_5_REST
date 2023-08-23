package org.example.repository;

import org.example.entity.OrderTableEntity;
import org.example.entity.ProductByOrderEntity;
import org.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductByOrderRepository extends JpaRepository<ProductByOrderEntity, Integer> {

    Optional<ProductByOrderEntity> findByProductEntityAndAndOrderTableEntity(ProductEntity productEntity, OrderTableEntity orderTableEntity);

    List<ProductByOrderEntity> findByOrderTableEntity(OrderTableEntity orderTableEntity);

    @Override
    <S extends ProductByOrderEntity> S save(S entity);

    @Override
    void delete(ProductByOrderEntity entity);
}
