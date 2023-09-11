package org.example.controller.adminController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.example.dto.CategoryDTO;
import org.example.entity.CategoryEntity;
import org.example.entity.ProductEntity;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.example.service.ProductService;
import org.example.util.CategoryValidatorAndConvert.CategoryValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RestController
@Tag(name = "Category Admin", description = "описание, бла-бла-бла")
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


    @PostMapping(value = "/admin/classifications/{nameClassification}/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createCategoryAdminPagePost(@PathVariable String nameClassification,
                                              @ModelAttribute("category") @Valid CategoryDTO categoryDTO,
                                              BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        categoryValidator.validate(categoryDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info(categoryDTO);
            if (categoryDTO.getFile().isEmpty() || categoryDTO.getFile() == null) {
                bindingResult.rejectValue("file", "", "Добавьте файл.");
            }
            ClassificationAdminController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setClassificationEntity(classificationService.findByName(nameClassification).get());
        categoryEntity.setDescription(categoryDTO.getDescription());
        categoryEntity.setStatus(categoryDTO.getStatus());
        categoryEntity.setName(categoryDTO.getName());
        if (categoryDTO.getFile() != null && !categoryDTO.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + categoryDTO.getFile().getOriginalFilename();
            try {
                categoryDTO.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String mainImaginePath = regex + resultFilename;
            categoryEntity.setPath(mainImaginePath);
        }
        categoryService.save(categoryEntity);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Category created successfully"));
    }


    @GetMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}")
    public ResponseEntity<Map<String, Object>> editCategoryAdminPageGet(@PathVariable String nameCategory, @PathVariable String nameClassification ) {
        CategoryDTO categoryDTO=new CategoryDTO();
        Map<String, Object> response = new HashMap<>();
        Optional<CategoryEntity> categoryEntity = categoryService.findByName(nameCategory);
        categoryDTO.setName(categoryEntity.get().getName());
        categoryDTO.setDescription(categoryEntity.get().getDescription());
        categoryDTO.setStatus(categoryEntity.get().getStatus());
        categoryDTO.setPath(categoryEntity.get().getPath());
        response.put("nameClassification",nameClassification);
        response.put("path", categoryEntity.get().getPath());
        response.put("categoryDTO", categoryDTO);
        response.put("listProducts", productService.findAllProductsByCategoryEntity(categoryEntity.get()));
//        response.put("contextPath", contextPath);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/admin/classifications/{nameClassification}/edit/{nameCategory}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> editCategoryAdminPagePut(@PathVariable String nameCategory, @PathVariable String nameClassification,
                                                                         @ModelAttribute("categoryDTO") @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
//        response.put("contextPath", contextPath);
//        response.put("nameClassification", nameClassification);
        Optional<CategoryEntity> categoryEntityOld = categoryService.findByName(nameCategory);
        log.info(nameCategory);
        log.info(categoryDTO);
        categoryValidator.validate(categoryDTO, bindingResult, nameCategory);
        if(classificationService.findByName(nameClassification).isEmpty()||classificationService.findByName(nameClassification).get().getCategoryEntityList().stream().map(s-> s.getName()).collect(Collectors.toList()).contains(nameCategory)){
            response.put("nameClassification","no exist");
            return ResponseEntity.badRequest().body(response);
        }
        if (bindingResult.hasErrors()) {
//            response.put("listProducts", productService.findAllProductsByCategoryEntity(categoryEntityOld.get()));
//            response.put("path", categoryEntityOld.get().getPath());
            ClassificationAdminController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        CategoryEntity categoryEntity = categoryService.findByName(nameCategory).get();
        categoryEntity.setClassificationEntity(classificationService.findByName(nameClassification).get());
        categoryEntity.setDescription(categoryDTO.getDescription());
        categoryEntity.setStatus(categoryDTO.getStatus());
        categoryEntity.setName(categoryDTO.getName());
        if (categoryDTO.getFile() != null && !categoryDTO.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + categoryDTO.getFile().getOriginalFilename();
            try {
                categoryDTO.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String mainImaginePath = regex + resultFilename;
            categoryEntity.setPath(mainImaginePath);
        }
        categoryService.save(categoryEntity);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Category edit successfully"));
    }

    @DeleteMapping("/admin/classifications/{nameClassification}/edit/{nameCategory}/delete/{nameProduct}")
    public ResponseEntity<String> deleteCategory(@PathVariable String nameCategory, @PathVariable String nameProduct, @PathVariable String nameClassification
                                                ) {
        log.info("DeleteMapping" + nameProduct + "start");
        Optional<ProductEntity> productEntity = productService.findByName(nameProduct);
        if (productEntity.isPresent()) {
            productService.delete(productEntity.get());
            return ResponseEntity.ok().body("Category deleted successfully");
        } else {
            log.error("Category deleted unsuccessfully");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/classifications/edit/{nameCategory}/getTableProducts")
    public ResponseEntity<Map<String, Object>>  getElementsTable(@PathVariable String nameCategory) {
        List<ProductEntity> productEntityList = productService.findAllProductsByCategoryEntity(categoryService.findByName(nameCategory).get());
        Map<String, Object> response = new HashMap<>();
        response.put("productEntityList", productEntityList);
        return ResponseEntity.ok().body(response);
    }

}
