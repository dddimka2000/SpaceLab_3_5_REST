package org.example.controller.publicController;

import org.example.entity.FavoriteProductEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.FavoriteProductService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WishListController {
    final
    FavoriteProductService favoriteProductService;

    public WishListController(FavoriteProductService favoriteProductService, ProductService productService) {
        this.favoriteProductService = favoriteProductService;
        this.productService = productService;
    }

    @GetMapping("/wishlist")
    public ResponseEntity<Map<String, Object>> showWishList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model){
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("favoriteProduct",favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity())
                    .stream().map(s->s.getProductEntity().getId()).toList());
        }
        response.put("products",favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity()).stream().map(s->s.getProductEntity()).toList());
        return ResponseEntity.ok().body(response);
    }
    final
    ProductService productService;


    @PostMapping("/products/{idProduct}/like")
    public ResponseEntity<Map<String, Object>> likeProduct(@PathVariable Integer idProduct, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Пользователь не залогинен"));
        }
        if (favoriteProductService.findByUserEntityAndProductEntity(userDetails
                .getUserEntity(), productService.findById(idProduct).get()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Попытка добавить дважды товар в избранное"));
        }
        if (productService.findById(idProduct).isPresent()) {
            FavoriteProductEntity favoriteProductEntity = new FavoriteProductEntity();
            favoriteProductEntity.setStatus(true);
            favoriteProductEntity.setUserEntity(userDetails.getUserEntity());
            favoriteProductEntity.setProductEntity(productService.findById(idProduct).get());
            favoriteProductService.save(favoriteProductEntity);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Product was like successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", false, "message", "Данного продукта не существует"));

    }


    @DeleteMapping("/products/{idProduct}/deleteLike")
    public ResponseEntity<Map<String, Object>> deleteLikeProduct(@PathVariable Integer idProduct, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Пользователь не залогинен"));
        }
        if (favoriteProductService.findByUserEntityAndProductEntity(userDetails
                .getUserEntity(), productService.findById(idProduct).get()).isPresent()) {
            favoriteProductService.delete(favoriteProductService.findByUserEntityAndProductEntity(userDetails
                    .getUserEntity(), productService.findById(idProduct).get()).get());
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Product was like successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", false, "message", "Данного продукта не существует"));
    }
}
