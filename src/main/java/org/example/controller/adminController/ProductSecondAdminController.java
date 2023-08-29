package org.example.controller.adminController;

import lombok.extern.log4j.Log4j2;
import org.example.dto.ProductDto;
import org.example.entity.ProductEntity;
import org.example.service.CategoryService;
import org.example.service.ProductService;
import org.example.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
public class ProductSecondAdminController {
    @Value("${spring.regex}")
    String regex;
    @Value("${spring.pathImg}")
    String path;
    private final
    CategoryService categoryService;

    private final
    ProductService productService;
    final
    ProductValidator productValidator;

    public ProductSecondAdminController(ProductService productService, CategoryService categoryService, ProductValidator productValidator) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productValidator = productValidator;
    }


    @PostMapping(value = "/admin/classifications/{nameClassification}/edit/{nameCategory}/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createProductSecondAdminPagePost(@PathVariable String nameCategory,
                                                                                @PathVariable String nameClassification,
                                                                                @ModelAttribute("productDTO") @Valid ProductDto productDTO,
                                                                                BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        productValidator.validate(productDTO, bindingResult);
        if (productDTO.getFile().isEmpty() || productDTO.getFile() == null) {
            bindingResult.rejectValue("file", "", "Добавьте картинку.");
        }
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
            response.put("fields", fieldErrors);
            return ResponseEntity.badRequest().body(response);
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setStatus(productDTO.getStatus());
        productEntity.setCategoryEntity(categoryService.findByName(nameCategory).get());
        if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + productDTO.getFile().getOriginalFilename();
            try {
                productDTO.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String mainImaginePath = regex + resultFilename;
            productEntity.setPath(mainImaginePath);
        }
        productService.save(productEntity);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Product edit - create"));
    }


    @GetMapping(value = "/admin/classifications/{nameClassification}/edit/{nameCategory}/edit/{nameProduct}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> showProductSecondAdminPageGet(@PathVariable String nameCategory, @PathVariable String nameClassification,
                                                                             @PathVariable String nameProduct) {
        ProductDto productDTO = new ProductDto();
        Map<String, Object> response = new HashMap<>();
        ProductEntity productEntity = productService.findByName(nameProduct).get();
        productDTO.setName(productEntity.getName());
        productDTO.setDescription(productEntity.getDescription());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setStatus(productEntity.getStatus());
        productDTO.setPath(productEntity.getPath());
        response.put("productEntity", productEntity);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/admin/classifications/{nameClassification}/edit/{nameCategory}/edit/{nameProduct}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> editProductSecondAdminPagePost(@PathVariable String nameCategory,
                                                                              @PathVariable String nameClassification,
                                                                              @ModelAttribute("productDTO") @Valid ProductDto productDTO,
                                                                              BindingResult bindingResult, @PathVariable String nameProduct) {
        Map<String, Object> response = new HashMap<>();
        log.info("nameProduct: " + nameProduct);
        ProductEntity productEntityOld = productService.findByName(nameProduct).get();
        productValidator.validate(productDTO, bindingResult, nameProduct);
        if (bindingResult.hasErrors()) {
            productDTO.setPath(productEntityOld.getPath());
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
            response.put("fields", fieldErrors);
            return ResponseEntity.badRequest().body(response);
        }
        ProductEntity productEntity = productService.findByName(nameProduct).get();
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setStatus(productDTO.getStatus());
        if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + productDTO.getFile().getOriginalFilename();
            try {
                productDTO.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String mainImaginePath = regex + resultFilename;
            productEntity.setPath(mainImaginePath);
        }
        productService.save(productEntity);

        return ResponseEntity.ok().body(Map.of("success", true, "message", "Product edit - edit"));
    }
}
