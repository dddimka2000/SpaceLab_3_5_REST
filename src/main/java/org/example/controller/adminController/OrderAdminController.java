package org.example.controller.adminController;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.example.dto.OrderTableDTO;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.entity.OrderTableEntity;
import org.example.entity.ProductByOrderEntity;
import org.example.service.*;
import org.example.util.OrderValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class OrderAdminController {
    final
    OrderTableService orderTableService;

    final
    CategoryService categoryService;
    final
    OrderValidator orderValidator;
    final
    UserEntityService userEntityService;
    final
    ProductByOrderService productByOrderService;
    final
    ClassificationService classificationService;
    final
    ProductService productService;


    public OrderAdminController(OrderTableService orderTableService, ClassificationService classificationService, CategoryService categoryService, ProductService productService, OrderValidator orderValidator, ProductByOrderService productByOrderService, UserEntityService userEntityService) {
        this.orderTableService = orderTableService;
        this.classificationService = classificationService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderValidator = orderValidator;
        this.productByOrderService = productByOrderService;
        this.userEntityService = userEntityService;
    }

    int pageSize = 3;


    @GetMapping("/admin/orders")
    public ResponseEntity<Map<String, Object>> showOrdersAdmin(@RequestParam(defaultValue = "0", name = "page") Integer page,
                                                               @RequestParam(defaultValue = "", name = "orderId") String orderId) {
        log.info("orderId " + orderId);
        Page<OrderTableEntity> orderTablePage = orderTableService.findByIdContaining(orderId, page, pageSize);
        List<OrderTableEntity> orderTableEntityList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        int totalPage = 0;
        if (orderTablePage != null) {
            totalPage = orderTablePage.getTotalPages();
            orderTableEntityList = orderTablePage.getContent();
        }
        response.put("orderTableEntityList", orderTableEntityList);
        response.put("currentPage", page);
        response.put("totalPages", totalPage);

        long count = orderTableService.countBy();
        String panelCount = "Показано " + (pageSize * page + 1) + "-" + (orderTableEntityList.size() + (pageSize * page)) + " из " + count;
        log.info(panelCount);
        response.put("panelCount", panelCount);
        response.put("orderId", orderId);

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/admin/orders/create")
    public ResponseEntity<Map<String, Object>> createOrderAdmin() {
        Map<String, Object> response = new HashMap<>();
        response.put("classifications", classificationService.findAllClassificationEntities().stream().map(classificationEntity -> classificationEntity.getName()).collect(Collectors.toList()));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<Map<String, Object>> editOrderAdmin(@PathVariable Integer id) {
        OrderTableDTO orderTableDTO=new OrderTableDTO();
        OrderTableEntity orderTableEntity = orderTableService.findById(id).get();
        Map<String, Object> response = new HashMap<>();
        orderTableDTO.setProducts(productByOrderService.findAllByOrderTableEntity(orderTableEntity).stream()
                .map(s -> s.getProductEntity().getName()).toList());
        log.info(orderTableDTO.getProducts());
        orderTableDTO.setQuantity(productByOrderService.findAllByOrderTableEntity(orderTableEntity).stream()
                .map(s -> String.valueOf(s.getCountProducts())).toList());
        log.info(orderTableDTO.getQuantity());


        orderTableDTO.setLogin(orderTableEntity.getUserEntity().getLogin());
//        model.addAttribute("idOrder"+id);
        orderTableDTO.setComment(orderTableEntity.getComment());
        orderTableDTO.setStatus(orderTableEntity.getStatus());
        response.put("classifications", classificationService.
                findAllClassificationEntities().stream().map(classificationEntity -> classificationEntity.getName()).collect(Collectors.toList()));
        response.put("orderTableDTO", orderTableDTO);
        return ResponseEntity.ok().body(response);
    }

    @ResponseBody
    @PutMapping(value = "/admin/orders/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> editOrderAdminPost(@ModelAttribute @Valid OrderTableDTO orderTableDTO,
                                                                  BindingResult bindingResult, @PathVariable Integer id) {
        log.info(orderTableDTO);
        orderTableDTO.setProducts(orderTableDTO.getProducts().stream()
                .map(product -> product.replaceAll("\\[", "")
                        .replaceAll("\"", "")
                        .replaceAll("]", ""))
                .collect(Collectors.toList()));

        orderTableDTO.setQuantity(orderTableDTO.getQuantity().stream()
                .map(s -> s.replaceAll("\\[", "")
                        .replaceAll("\"", "")
                        .replaceAll("]", ""))
                .collect(Collectors.toList()));

        orderValidator.validate(orderTableDTO, bindingResult);
        Map response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            ClassificationAdminController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        OrderTableEntity orderTableEntity = orderTableService.findById(id).get();
        orderTableEntity.setStatus(orderTableDTO.getStatus());
        orderTableEntity.setComment(orderTableDTO.getComment());
        orderTableEntity.setUserEntity(userEntityService.findByLogin(orderTableDTO.getLogin()).get());
        orderTableService.save(orderTableEntity);
        productByOrderService.findAllByOrderTableEntity(orderTableEntity).forEach(s -> productByOrderService.delete(s));
        AtomicInteger num = new AtomicInteger(0);
        orderTableDTO.getQuantity().stream().forEach(s -> {
            ProductByOrderEntity productByOrderEntity = new ProductByOrderEntity();
            productByOrderEntity.setOrderTableEntity(orderTableEntity);
            productByOrderEntity.setCountProducts(Integer.parseInt(s));
            productByOrderEntity.setProductEntity(productService.findByName(orderTableDTO.getProducts().get(num.get())).get());
            num.incrementAndGet();
            productByOrderService.save(productByOrderEntity);
        });

        return ResponseEntity.ok().body(Map.of("success", true, "message", "Product created successfully"));
    }


    @ResponseBody
    @PostMapping(value = "/admin/orders/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createOrderAdminPost(@ModelAttribute @Valid OrderTableDTO orderTableDTO,
                                                                    BindingResult bindingResult) {
        log.info(orderTableDTO);
        orderTableDTO.setProducts(orderTableDTO.getProducts().stream()
                .map(product -> product.replaceAll("\\[", "")
                        .replaceAll("\"", "")
                        .replaceAll("]", ""))
                .collect(Collectors.toList()));

        orderTableDTO.setQuantity(orderTableDTO.getQuantity().stream()
                .map(s -> s.replaceAll("\\[", "")
                        .replaceAll("\"", "")
                        .replaceAll("]", ""))
                .collect(Collectors.toList()));

        orderValidator.validate(orderTableDTO, bindingResult);
        Map response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            ClassificationAdminController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        OrderTableEntity orderTableEntity = new OrderTableEntity();
        orderTableEntity.setStatus(orderTableDTO.getStatus());
        orderTableEntity.setComment(orderTableDTO.getComment());
        orderTableEntity.setUserEntity(userEntityService.findByLogin(orderTableDTO.getLogin()).get());
        orderTableEntity.setDateTime(LocalDateTime.now());
        orderTableService.save(orderTableEntity);
        AtomicInteger num = new AtomicInteger(0);
        orderTableDTO.getQuantity().stream().forEach(s -> {
            ProductByOrderEntity productByOrderEntity = new ProductByOrderEntity();
            productByOrderEntity.setOrderTableEntity(orderTableEntity);
            productByOrderEntity.setCountProducts(Integer.parseInt(s));
            productByOrderEntity.setProductEntity(productService.findByName(orderTableDTO.getProducts().get(num.get())).get());
            num.incrementAndGet();
            productByOrderService.save(productByOrderEntity);
        });

        return ResponseEntity.ok().body(Map.of("success", true, "message", "Product created successfully"));
    }


    @GetMapping("/admin/orders/create/getCategories")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adminProductsEditShow(@RequestParam(name = "selectedValue") String selectedValue) {
        Map<String, Object> response = new HashMap<>();
        Optional<ClassificationEntity> classificationEntityOptional = classificationService.findByName(selectedValue);
        List<String> categoryEntityList = null;
        if (classificationEntityOptional.isPresent()) {
            categoryEntityList = categoryService.findAllCategoriesByClassificationEntity(classificationEntityOptional.get()).stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.toList());
            log.info(categoryEntityList);
        }
        response.put("categoryEntityList", categoryEntityList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/orders/create/getProducts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adminProductsShowByCategory(@RequestParam(name = "selectedValue") String selectedValue) {
        Map<String, Object> response = new HashMap<>();
        Optional<CategoryEntity> categoryEntity = categoryService.findByName(selectedValue);
        List<String> productEntityList = null;
        if (categoryEntity.isPresent()) {
            productEntityList = productService.findAllProductsByCategoryEntity(categoryEntity.get()).stream().map(productEntity -> productEntity.getName()).collect(Collectors.toList());
            log.info(productEntityList);
        }
        response.put("productEntityList", productEntityList);
        return ResponseEntity.ok().body(response);
    }
}
