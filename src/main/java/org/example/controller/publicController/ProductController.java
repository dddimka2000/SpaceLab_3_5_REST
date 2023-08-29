package org.example.controller.publicController;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.dto.ProductDto;
import org.example.entity.ProductEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.FavoriteProductService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    final
    ProductService productService;

    public ProductController(ProductService productService, FavoriteProductService favoriteProductService) {
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    final
    FavoriteProductService favoriteProductService;

    @GetMapping("/productInfo/{idProduct}")
    public ResponseEntity<Map<String, Object>> showInfoProduct(@PathVariable Integer idProduct, @ModelAttribute("productDTO") ProductDto productDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("favoriteProduct", favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity())
                    .stream().map(s -> s.getProductEntity().getId()).toList());
        }
        if (productService.findById(idProduct).get().getStatus() == false) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Product edit - create"));
        }
        ProductEntity productEntity = productService.findById(idProduct).get();
        productDto.setId(productEntity.getId());
        productDto.setName(productEntity.getName());
        productDto.setDescription(productEntity.getDescription());
        productDto.setPath(productEntity.getPath());
        productDto.setPrice(productEntity.getPrice());
        List<ProductEntity> products = productService.findAllProductsByCategoryEntity(productEntity.getCategoryEntity());
        Collections.shuffle(products);
        response.put("otherProducts", products.stream().limit(4));
        return ResponseEntity.ok().body(response);
    }
}
