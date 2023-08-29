package org.example.controller.publicController;

import lombok.extern.log4j.Log4j2;
import org.example.entity.BasketItemEntity;
import org.example.entity.ProductEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.BasketItemService;
import org.example.service.FavoriteProductService;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Log4j2
public class BasketController {
    private final
    BasketItemService basketItemService;

    public BasketController(BasketItemService basketItemService, ProductService productService, FavoriteProductService favoriteProductService) {
        this.basketItemService = basketItemService;
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    final
    ProductService productService;
    final
    FavoriteProductService favoriteProductService;


    @GetMapping("/basket")
    public ResponseEntity<Map<String, Object>> showWishList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("favoriteProduct", favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity())
                    .stream().map(s -> s.getProductEntity().getId()).toList());
        }
        response.put("count", basketItemService.findAllBasketProductsByUserEntity(userDetails.getUserEntity())
                .stream().map(s -> s.getCount()).toList());
        response.put("products", basketItemService.findAllBasketProductsByUserEntity(userDetails.getUserEntity()).stream().map(s -> s.getProductEntity()).toList());
        return ResponseEntity.ok().body(response);
    }

    @ResponseBody
    @PostMapping("/product/{idProduct}/basketItem/add")
    public ResponseEntity<Map<String, Object>> addToBasket(@RequestParam("count") Integer count
            , @PathVariable Integer idProduct, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Пользователь не залогинен"));
        }
        if (count != null && count < 1000) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Кол-во может быть от 1 до 999"));
        }
        Optional<ProductEntity> productEntity = productService.findById(idProduct);
        if (productEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Такого продукта не существует"));
        }
        Optional<BasketItemEntity> basketItemEntityCheck = basketItemService.findByUserEntityAndProductEntity(userDetails.getUserEntity(), productEntity.get());
        if (basketItemEntityCheck.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Товар уже добавлен в корзину"));
        }

        BasketItemEntity basketItemEntity = new BasketItemEntity();
        basketItemEntity.setProductEntity(productEntity.get());
        basketItemEntity.setUserEntity(userDetails.getUserEntity());
        basketItemEntity.setCount(count);
        basketItemService.save(basketItemEntity);

        return ResponseEntity.ok().body(Map.of("success", true, "message", "Продукт добавлен в корзину"));

    }


    @DeleteMapping("/product/{idProduct}/basketItem/delete")
    public ResponseEntity<Map<String, Object>> deleteToBasket(@PathVariable Integer idProduct, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Пользователь не залогинен"));
        }
        Optional<ProductEntity> productEntity = productService.findById(idProduct);
        if (productEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Такого продукта не существует"));
        }
        Optional<BasketItemEntity> basketItemEntity = basketItemService.findByUserEntityAndProductEntity(userDetails.getUserEntity(), productEntity.get());
        if (basketItemEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Такого продукта не существует в корзине пользователя"));
        }
        basketItemService.delete(basketItemEntity.get());
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Продукт удален из корзины"));
    }

    @PutMapping("/product/{idProduct}/basketItem/put")
    public ResponseEntity<Map<String, Object>> deleteToBasket(@RequestParam("count") Integer count
            ,@PathVariable Integer idProduct, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Пользователь не залогинен"));
        }
        Optional<ProductEntity> productEntity = productService.findById(idProduct);
        if (productEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Такого продукта не существует"));
        }
        Optional<BasketItemEntity> basketItemEntity = basketItemService.findByUserEntityAndProductEntity(userDetails.getUserEntity(), productEntity.get());
        if (basketItemEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Такого продукта не существует в корзине пользователя"));
        }
        if (count<1 && count>999) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Кол-во может быть от 1 до 999 единиц"));
        }
        basketItemEntity.get().setCount(count);
        basketItemService.save(basketItemEntity.get());
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Кол-во продуктов обновлено"));
    }
}
