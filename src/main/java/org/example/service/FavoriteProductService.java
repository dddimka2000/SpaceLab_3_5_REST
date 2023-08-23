package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.*;
import org.example.repository.FavoriteProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FavoriteProductService {
    private final
    FavoriteProductRepository favoriteProductRepository;

    public FavoriteProductService(FavoriteProductRepository favoriteProductRepository) {
        this.favoriteProductRepository = favoriteProductRepository;
    }

    public Map<String, Long> findBestSeven() {
        List<FavoriteProductEntity> favoriteProducts = favoriteProductRepository.findAll();

        Map<String, Long> productCounts = favoriteProducts.stream()
                .collect(Collectors.groupingBy(favoriteProductEntity -> favoriteProductEntity.getProductEntity().getName(), Collectors.counting()));


        return productCounts;
    }

    public Optional<FavoriteProductEntity> findById(Integer id) {
        log.info("FavoriteProductEntity-findById start: " + id);
        Optional<FavoriteProductEntity> favoriteProductEntity = favoriteProductRepository.findById(id);
        if (favoriteProductEntity.isPresent()) {
            log.info("FavoriteProductEntity-findById successful: " + favoriteProductEntity.get().getId());
        } else {
            log.warn("FavoriteProductEntity empty");
        }
        return favoriteProductEntity;
    }

    public void delete(FavoriteProductEntity favoriteProductEntity) {
        log.info("FavoriteProductEntity-delete: " + favoriteProductEntity.getId());
        favoriteProductRepository.delete(favoriteProductEntity);
        log.info("FavoriteProductEntity-delete successful");
    }

    public void save(FavoriteProductEntity favoriteProductEntity) {
        log.info("FavoriteProductEntity-save start: " + favoriteProductEntity.getId());
        favoriteProductRepository.save(favoriteProductEntity);
        log.info("FavoriteProductEntity-save successful");
    }

    public List<FavoriteProductEntity> findAllFavoriteProductsByUserEntity(UserEntity user) {
        log.info("FavoriteProductEntity-findAllCategoriesByClassificationEntity");
        List<FavoriteProductEntity> favoriteProductEntity = favoriteProductRepository.findByUserEntity(user);
        log.info("FavoriteProductEntity-findAllCategoriesByClassificationEntity successful: " + user.getId());
        return favoriteProductEntity;
    }

    public Optional<FavoriteProductEntity> findByUserEntityAndProductEntity(UserEntity user, ProductEntity productEntity) {
        log.info("FavoriteProductEntity-findAllCategoriesByClassificationEntity");
        Optional<FavoriteProductEntity> favoriteProductEntity = favoriteProductRepository.findByUserEntityAndProductEntity(user, productEntity);
        log.info("FavoriteProductEntity-findAllCategoriesByClassificationEntity successful: " + user.getId());
        return favoriteProductEntity;
    }
}
