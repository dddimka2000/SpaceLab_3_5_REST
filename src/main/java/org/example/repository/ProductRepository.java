package org.example.repository;

import org.example.entity.CategoryEntity;
import org.example.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Override
    <S extends ProductEntity> S save(S entity);

    Optional<ProductEntity> findById(Integer integer);

    @Override
    void delete(ProductEntity entity);

    List<ProductEntity> findByCategoryEntity(CategoryEntity categoryEntity);

    Optional<ProductEntity> findByName(String name);

    @Override
    Page<ProductEntity> findAll(Pageable pageable);

    Page<ProductEntity> findByNameContainingIgnoreCase(String productName, Pageable pageable);
    Page<ProductEntity> findByNameContainingIgnoreCaseAndStatus(String productName, Pageable pageable,Boolean status);


    Long countBy();
    Long countByStatusAndNameContainingIgnoreCase(Boolean status,String productName);

}
