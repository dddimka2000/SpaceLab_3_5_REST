package org.example.controller.publicController;

import lombok.extern.log4j.Log4j2;
import org.example.dto.CategoryDTO;
import org.example.entity.CategoryEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.example.service.FavoriteProductService;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class CatalogController {
    final
    ClassificationService classificationService;
    final
    CategoryService categoryService;
    final
    ProductService productService;
    private final
    FavoriteProductService favoriteProductService;


    public CatalogController(ClassificationService classificationService, CategoryService categoryService, ProductService productService, FavoriteProductService favoriteProductService) {
        this.classificationService = classificationService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    @GetMapping("/catalog")
    public ResponseEntity<Map<String, Object>> showCatalog() {
        Map<String, Object> response = new HashMap<>();
        Map<String, List<CategoryEntity>> map = classificationService
                .findAllClassificationEntities().stream().filter(s -> s.getStatus() == true).collect(Collectors.toMap(classificationEntity -> classificationEntity.getName(),
                        classificationEntity -> categoryService.findAllCategoriesByClassificationEntity(classificationEntity)));
        response.put("carouselItems", classificationService
                .findAllClassificationEntities().stream().filter(s -> s.getStatus() == true).map(s -> s.getPath()).toList());
        response.put("categoryMap", map);
        log.info(map.size());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/catalog/{idCategory}")
    public  ResponseEntity<Map<String, Object>> showProductsByCatalog( @PathVariable Integer idCategory, @ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("favoriteProduct",favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity())
                .stream().map(s->s.getProductEntity().getId()).toList());
        }
        CategoryEntity categoryEntity = categoryService.findById(idCategory).get();
        categoryDTO.setDescription(categoryEntity.getDescription());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setPath(categoryEntity.getPath());
        response.put("products", productService.findAllProductsByCategoryEntity(categoryEntity));
        return ResponseEntity.ok().body(response);
    }


}