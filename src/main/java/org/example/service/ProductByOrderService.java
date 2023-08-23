package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.OrderTableEntity;
import org.example.entity.ProductByOrderEntity;
import org.example.entity.ProductEntity;
import org.example.repository.ProductByOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ProductByOrderService {
    private final
    ProductByOrderRepository productByOrderRepository;

    public ProductByOrderService(ProductByOrderRepository productByOrderRepository) {
        this.productByOrderRepository = productByOrderRepository;
    }


    public void save(ProductByOrderEntity productByOrder) {
        log.info("productByOrder-save start: " + productByOrder.getId());
        productByOrderRepository.save(productByOrder);
        log.info("productByOrder-save successful");
    }

    public List<ProductByOrderEntity> findAllByOrderTableEntity(OrderTableEntity orderTableEntity) {
        log.info("ProductService-findAllProductsByCategoryEntity by "+orderTableEntity.getId());
        List<ProductByOrderEntity> productByOrderList = productByOrderRepository.findByOrderTableEntity(orderTableEntity);
        log.info("ProductService-findAllProductsByCategoryEntity successful");
        return productByOrderList;
    }
    public Optional<ProductByOrderEntity> findByProductEntityAndAndOrderTableEntity(ProductEntity productEntity, OrderTableEntity orderTableEntity) {
        log.info("ProductService-findByProductEntityAndAndOrderTableEntity");
        Optional<ProductByOrderEntity> productByOrderList = productByOrderRepository.findByProductEntityAndAndOrderTableEntity(productEntity, orderTableEntity);
        log.info("ProductService-findByProductEntityAndAndOrderTableEntity successful: " + productByOrderList);
        return productByOrderList;
    }
    public void delete(ProductByOrderEntity productByOrder) {
        log.info("ProductService- delete: " + productByOrder.getId());
        productByOrderRepository.delete(productByOrder);
        log.info("ProductService-delete successful");
    }
}
