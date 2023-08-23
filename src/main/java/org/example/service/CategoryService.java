package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Log4j2
@Service
public class CategoryService {
    private final
    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryEntity> findById(Integer id) {
        log.info("CategoryService-findById start: " + id);
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (categoryEntity.isPresent()) {
            log.info("CategoryService-findById successful: " + categoryEntity);
        } else {
            log.warn("CategoryEntity empty");
        }
        return categoryEntity;
    }


    public void save(CategoryEntity classificationEntity) {
        log.info("CategoryService-save start: " + classificationEntity);
        categoryRepository.save(classificationEntity);
        log.info("CategoryService-save successful");
    }

    public List<CategoryEntity> findAllCategoriesByClassificationEntity(ClassificationEntity classificationEntity) {
        log.info("CategoryService-findAllCategoriesByClassificationEntity");
        List<CategoryEntity> categoryEntityList = categoryRepository.findByClassificationEntity(classificationEntity);
        log.info("CategoryService-findAllCategoriesByClassificationEntity successful: " + categoryEntityList);
        return categoryEntityList;
    }

    public Optional<CategoryEntity> findByName(String name) {
        log.info("CategoryService-findByName start: " + name);
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByName(name);
        if (categoryEntity.isPresent()) {
            log.info("CategoryService-findByName successful: " + categoryEntity);
        } else {
            log.warn("CategoryEntity empty");
        }
        return categoryEntity;
    }

    public void delete(CategoryEntity classificationEntity) {
        log.info("CategoryService-delete: " + classificationEntity);
        categoryRepository.delete(classificationEntity);
        log.info("CategoryService-delete successful");
    }
}
