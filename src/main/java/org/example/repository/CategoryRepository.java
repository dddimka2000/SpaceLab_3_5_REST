package org.example.repository;

import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {
    List<CategoryEntity> findByClassificationEntity(ClassificationEntity classificationEntity);

    @Override
    <S extends CategoryEntity> S save(S entity);

    @Override
    Optional<CategoryEntity> findById(Integer integer);

    @Override
    void delete(CategoryEntity entity);

    Optional<CategoryEntity> findByName(String name);
}
