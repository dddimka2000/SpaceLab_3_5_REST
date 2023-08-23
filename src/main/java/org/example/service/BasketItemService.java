package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.BasketItemEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.example.repository.BasketItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BasketItemService {
    private final
    BasketItemRepository basketItemRepository;

    public BasketItemService(BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
    }


    public Optional<BasketItemEntity> findById(Integer id) {
        log.info("BasketItemEntity-findById start: " + id);
        Optional<BasketItemEntity> basketItemEntity = basketItemRepository.findById(id);
        if (basketItemEntity.isPresent()) {
            log.info("BasketItemEntity-findById successful: " + basketItemEntity.get().getId());
        } else {
            log.warn("BasketItemEntity empty");
        }
        return basketItemEntity;
    }

    public void delete(BasketItemEntity basketItemEntity) {
        log.info("BasketItemEntity-delete: " + basketItemEntity.getId());
        basketItemRepository.delete(basketItemEntity);
        log.info("BasketItemEntity-delete successful");
    }

    public void save(BasketItemEntity basketItemEntity) {
        log.info("BasketItemEntity-save start: " + basketItemEntity.getId());
        basketItemRepository.save(basketItemEntity);
        log.info("BasketItemEntity-save successful");
    }

    public List<BasketItemEntity> findAllBasketProductsByUserEntity(UserEntity user) {
        log.info("BasketItemEntity-findAllCategoriesByClassificationEntity");
        List<BasketItemEntity> basketItemEntity = basketItemRepository.findByUserEntity(user);
        log.info("BasketItemEntity-findAllCategoriesByClassificationEntity successful: " + user.getId());
        return basketItemEntity;
    }

    public Optional<BasketItemEntity> findByUserEntityAndProductEntity(UserEntity user, ProductEntity productEntity) {
        log.info("BasketItemEntity-findAllCategoriesByClassificationEntity");
        Optional<BasketItemEntity> basketItemEntity = basketItemRepository.findByUserEntityAndProductEntity(user,productEntity);
        log.info("BasketItemEntity-findAllCategoriesByClassificationEntity successful: " + user.getId());
        return basketItemEntity;
    }
}
