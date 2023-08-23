package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Log4j2
@Service
public class ProductService {
    private final
    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<ProductEntity> findByName(String name) {
        log.info("ProductService-findByName start: " + name);
        Optional<ProductEntity> productEntity = productRepository.findByName(name);
        if (productEntity.isPresent()) {
            log.info("ProductService-findByName successful: " + productEntity);
        } else {
            log.info("productEntity empty");
        }
        return productEntity;
    }

    public void save(ProductEntity productEntity) {
        log.info("ProductEntity-save start: " + productEntity.getId());
        productRepository.save(productEntity);
        log.info("ProductEntity-save successful");
    }

    public List<ProductEntity> findAllProductsByCategoryEntity(CategoryEntity categoryEntity) {
        log.info("ProductService-findAllProductsByCategoryEntity by "+categoryEntity.getId());
        List<ProductEntity> productEntityList = productRepository.findByCategoryEntity(categoryEntity);
        log.info("ProductService-findAllProductsByCategoryEntity successful ");
        return productEntityList;
    }
    public void delete(ProductEntity productEntity) {
        log.info("ProductService- delete: " + productEntity.getId());
        productRepository.delete(productEntity);
        log.info("ProductService-delete successful");
    }
    public Page<ProductEntity> findAllProductsPage(Integer pageNumber, Integer pageSize) {
        log.info("ProductService-findAllProductsPage start");
        Page<ProductEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = productRepository.findAll(pageable);
        log.info("ProductsEntities with " + pageNumber + " have been found");
        log.info("ProductService-findAllProductsPage successfully");
        return page;

    }

    public Page<ProductEntity> findByNameContainingIgnoreCase(String name, Integer pageNumber, Integer pageSize) {
        log.info("ProductService-findByNameContainingIgnoreCase start");
        Page<ProductEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = productRepository.findByNameContainingIgnoreCase(name,pageable);
        log.info("ProductsEntities with " + pageNumber + " and "+ name+" have been found");
        log.info("ProductService-findByNameContainingIgnoreCase successfully");
        return page;
    }
    public Page<ProductEntity> findByNameContainingIgnoreCaseWithStatus(String name, Integer pageNumber, Integer pageSize,Boolean status) {
        log.info("ProductService-findByNameContainingIgnoreCaseWithStatus start");
        Page<ProductEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = productRepository.findByNameContainingIgnoreCaseAndStatus(name,pageable,status);
        log.info("ProductsEntities with " + pageNumber + " and "+ name+" have been found");
        log.info("ProductService-findByNameContainingIgnoreCaseWithStatus successfully");
        return page;
    }


    public Optional<ProductEntity> findById(Integer integer){
        log.info("ProductService-findById start: " + integer);
        Optional<ProductEntity> productEntity=productRepository.findById(integer);
        if (productEntity.isPresent()) {
            log.info("ProductService-findById successful: " + productEntity.get().getId());
        } else {
            log.info("productEntity empty");
        }
        return productEntity;
    }

    public long countBy(){
        log.info("ProductService-countBy");
        long count=productRepository.countBy();
        log.info("ProductService-countBy get: "+count);
        return count;
    }

    public long countByStatusAndNameContainingIgnoreCase(Boolean bool,String name){
        log.info("ProductService-countBy");
        long count=productRepository.countByStatusAndNameContainingIgnoreCase(bool,name);
        log.info("ProductService-countBy get: "+count);
        return count;
    }
}
