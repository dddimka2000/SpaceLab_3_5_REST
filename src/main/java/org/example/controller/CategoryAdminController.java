package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.example.dto.CategoryDTO;
import org.example.dto.ClassificationDTO;
import org.example.entity.CategoryEntity;
import org.example.entity.ProductEntity;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.example.service.ProductService;
import org.example.util.CategoryValidatorAndConvert.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;


import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Log4j2
@RestController
@Api(tags = "Category Admin API", description = "API endpoints for managing categories by admins")
public class CategoryAdminController {
    @Value("${spring.regex}")
    String regex;
    @Value("${spring.pathImg}")
    String path;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final
    CategoryService categoryService;
    private final
    ClassificationService classificationService;
    private final
    CategoryValidator categoryValidator;

    private final
    ProductService productService;

    public CategoryAdminController(CategoryService categoryService, ClassificationService classificationService, CategoryValidator categoryValidator, ProductService productService) {
        this.categoryService = categoryService;
        this.classificationService = classificationService;
        this.categoryValidator = categoryValidator;
        this.productService = productService;
    }
    @ApiOperation(value = "create classifications")
    @GetMapping("/admin/classifications/{nameClassification}/create")
    public ResponseEntity<Map<String, Object>> createCategoryAdminPageShow(@ApiParam(value = "nameClassification data")@PathVariable String nameClassification,
                                                                           @ApiParam(value = "category data")@ModelAttribute("category") CategoryDTO categoryDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("nameClassification", nameClassification);
        return ResponseEntity.ok().body(response);
    }
//
//    @PostMapping("/admin/classifications/{nameClassification}/create")
//    public ResponseEntity<Map<String, Object>> createCategoryAdminPagePost(@PathVariable String nameClassification,
//                                              @ModelAttribute("category") @Valid CategoryDTO categoryDTO,
//                                              BindingResult bindingResult) {
//        Map<String, Object> response = new HashMap<>();
//
//        categoryValidator.validate(categoryDTO, bindingResult);
//        if (bindingResult.hasErrors()) {
//            log.info(categoryDTO);
//            if (categoryDTO.getFile().isEmpty() || categoryDTO.getFile() == null) {
//                bindingResult.rejectValue("file", "", "Добавьте файл.");
//            }
//            Map<String, String> fieldErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                fieldErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
//            response.put("fields", fieldErrors);
//            return ResponseEntity.badRequest().body(response);
//        }
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setClassificationEntity(classificationService.findByName(nameClassification).get());
//        categoryEntity.setDescription(categoryDTO.getDescription());
//        categoryEntity.setStatus(categoryDTO.getStatus());
//        categoryEntity.setName(categoryDTO.getName());
//        if (categoryDTO.getFile() != null && !categoryDTO.getFile().isEmpty()) {
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFilename = uuidFile + "." + categoryDTO.getFile().getOriginalFilename();
//            try {
//                categoryDTO.getFile().transferTo(new File(path + resultFilename));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            String mainImaginePath = regex + resultFilename;
//            categoryEntity.setPath(mainImaginePath);
//        }
//        categoryService.save(categoryEntity);
//        return ResponseEntity.ok().body(response);
//    }
//
//    String nameCategoryOld = null;
//
//    @GetMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}")
//    public ResponseEntity<Map<String, Object>> editCategoryAdminPageGet(@PathVariable String nameCategory, @PathVariable String nameClassification,
//                                                                         @ModelAttribute("categoryDTO") CategoryDTO categoryDTO) {
//        Map<String, Object> response = new HashMap<>();
//
//        log.info("wwwwwwwwwwwwwwwwwww" + contextPath);
//        nameCategoryOld = nameCategory;
//        Optional<CategoryEntity> categoryEntity = categoryService.findByName(nameCategoryOld);
//        categoryDTO.setName(categoryEntity.get().getName());
//        categoryDTO.setDescription(categoryEntity.get().getDescription());
//        categoryDTO.setStatus(categoryEntity.get().getStatus());
//        categoryDTO.setPath(categoryEntity.get().getPath());
//
//        response.put("path", categoryEntity.get().getPath());
//        response.put("categoryDTO", categoryDTO);
//        response.put("listProducts", productService.findAllProductsByCategoryEntity(categoryEntity.get()));
////        response.put("contextPath", contextPath);
//        return ResponseEntity.ok().body(response);
//    }
//
//    @PutMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}")
//    public ResponseEntity<Map<String, Object>> editCategoryAdminPagePOST(@PathVariable String nameCategory, @PathVariable String nameClassification,
//                                                                      @ModelAttribute("categoryDTO") @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
//        Map<String, Object> response = new HashMap<>();
//
//
////        response.put("contextPath", contextPath);
////        response.put("nameClassification", nameClassification);
//        Optional<CategoryEntity> categoryEntityOld = categoryService.findByName(nameCategoryOld);
//        log.info(nameCategoryOld);
//        categoryValidator.validate(categoryDTO, bindingResult, nameCategoryOld);
//        if (bindingResult.hasErrors()) {
////            response.put("listProducts", productService.findAllProductsByCategoryEntity(categoryEntityOld.get()));
////            response.put("path", categoryEntityOld.get().getPath());
//            Map<String, String> fieldErrors = new HashMap<>();
//            for (FieldError error : bindingResult.getFieldErrors()) {
//                fieldErrors.put(error.getField(), error.getDefaultMessage());
//            }
//            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
//            response.put("fields", fieldErrors);
//            return ResponseEntity.badRequest().body(response);
//        }
//        CategoryEntity categoryEntity = categoryService.findByName(nameCategoryOld).get();
//        categoryEntity.setClassificationEntity(classificationService.findByName(nameClassification).get());
//        categoryEntity.setDescription(categoryDTO.getDescription());
//        categoryEntity.setStatus(categoryDTO.getStatus());
//        categoryEntity.setName(categoryDTO.getName());
//        if (categoryDTO.getFile() != null && !categoryDTO.getFile().isEmpty()) {
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFilename = uuidFile + "." + categoryDTO.getFile().getOriginalFilename();
//            try {
//                categoryDTO.getFile().transferTo(new File(path + resultFilename));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            String mainImaginePath = regex + resultFilename;
//            categoryEntity.setPath(mainImaginePath);
//        }
//        categoryService.save(categoryEntity);
//        return ResponseEntity.ok().body(Map.of("success", true, "message", "Category created successfully"));
//    }
//
//    @DeleteMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}/delete/{nameProduct}")
//    public ResponseEntity<String> deleteCategory(@PathVariable String nameCategory, @PathVariable String nameProduct, @PathVariable String nameClassification
//                                                ) {
//        log.info("DeleteMapping" + nameProduct + "start");
//        Optional<ProductEntity> productEntity = productService.findByName(nameProduct);
//        if (productEntity.isPresent()) {
//            productService.delete(productEntity.get());
//            return ResponseEntity.ok().body("Category deleted successfully");
//        } else {
//            log.error("Category deleted unsuccessfully");
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}/getTableProducts")
//    public ResponseEntity<Map<String, Object>>  getElementsTable(@PathVariable String nameClassification, @PathVariable String nameCategory) {
//        List<ProductEntity> productEntityList = productService.findAllProductsByCategoryEntity(categoryService.findByName(nameCategory).get());
//        Map<String, Object> response = new HashMap<>();
//        response.put("productEntityList", productEntityList);
//        return ResponseEntity.ok().body(response);
//    }

}
