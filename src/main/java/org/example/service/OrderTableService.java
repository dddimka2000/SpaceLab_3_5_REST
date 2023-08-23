package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.OrderTableEntity;
import org.example.entity.UserEntity;
import org.example.repository.OrderTableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderTableService {
    private final
    OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Optional<OrderTableEntity> findById(Integer id) {
        log.info("OrderTableService-findById start: " + id);
        Optional<OrderTableEntity> orderTableEntity = orderTableRepository.findById(id);
        if (orderTableEntity.isPresent()) {
            log.info("OrderTableService-findById successful: " + orderTableEntity.get().getId());
        } else {
            log.info("OrderTableEntity empty");
        }
        return orderTableEntity;
    }

    public void save(OrderTableEntity OrderTableEntity) {
        log.info("OrderTableEntity-save start: " + OrderTableEntity.getId());
        orderTableRepository.save(OrderTableEntity);
        log.info("OrderTableEntity-save successful");
    }

    public Page<OrderTableEntity> findAllOrderTableEntitiesByUserEntity(String idOrder, Integer pageNumber, Integer pageSize, UserEntity userEntity) {
        Page<OrderTableEntity> page = null;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        try {
            int id = Integer.parseInt(idOrder);
            log.info("OrderTableEntity with " + pageNumber + " and " + id);
            page = orderTableRepository.findByIdAndUserEntity(id, pageable, userEntity);
            log.info("OrderTableEntity-findAllOrderTableEntitiesByUserEntity successfully");
        } catch (NumberFormatException e) {
            page = orderTableRepository.findAllByUserEntity(pageable,userEntity);
            log.warn("OrderTableEntity-findAllOrderTableEntitiesByUserEntity unsuccessfully");
        }
        return page;
    }

    public void delete(OrderTableEntity orderTableEntity) {
        log.info("OrderTableService- delete: " + orderTableEntity.getId());
        orderTableRepository.delete(orderTableEntity);
        log.info("OrderTableService-delete successful");
    }

    public Page<OrderTableEntity> findAllOrdersPage(Integer pageNumber, Integer pageSize) {
        log.info("OrderTableService-findAllOrdersPage start");
        Page<OrderTableEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = orderTableRepository.findAll(pageable);
        log.info("OrdersEntities with " + pageNumber + " have been found");
        log.info("OrderTableService-findAllOrdersPage successfully");
        return page;

    }

    public Page<OrderTableEntity> findByIdContaining(String idOrder, Integer pageNumber, Integer pageSize) {
        log.info("OrderTableService-findByIdContainingIgnoreCase start");
        Page<OrderTableEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        try {
            int id = Integer.parseInt(idOrder);
            log.info("OrderTableEntity with " + pageNumber + " and " + id);
            page = orderTableRepository.findById(id, pageable);
            log.info("OrderTableEntity-findByIdContainingIgnoreCase successfully");
        } catch (NumberFormatException e) {
            page = orderTableRepository.findAll(pageable);
            log.warn("OrderTableEntity-findByIdContainingIgnoreCase unsuccessfully");
        }
        return page;
    }


    public long countBy() {
        log.info("OrderTableService-countBy");
        long count = orderTableRepository.countBy();
        log.info("OrderTableService-countBy get: " + count);
        return count;
    }
    public long countByUserEntity(UserEntity user) {
        log.info("OrderTableService-countByUserEntity");
        long count = orderTableRepository.countByUserEntity(user);
        log.info("OrderTableService-countByUserEntity get: " + count);
        return count;
    }
}
