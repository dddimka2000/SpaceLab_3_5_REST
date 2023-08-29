package org.example.controller.publicController;

import lombok.extern.log4j.Log4j2;
import org.example.entity.ProductEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.FavoriteProductService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
public class MainPageController {
    final
    ProductService productService;


    private final
    FavoriteProductService favoriteProductService;

    int size=6;

    public MainPageController(ProductService productService, FavoriteProductService favoriteProductService) {
        this.productService = productService;
        this.favoriteProductService = favoriteProductService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> showMainPage(@RequestParam(defaultValue = "0", name = "page") Integer page,
                                                            @RequestParam(defaultValue = "", name = "productName") String productName,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails != null) {
            response.put("favoriteProduct",favoriteProductService.findAllFavoriteProductsByUserEntity(userDetails.getUserEntity())
                    .stream().map(s->s.getProductEntity().getId()).toList());
        }
        Page<ProductEntity> productsPage = productService.findByNameContainingIgnoreCaseWithStatus(productName, page, size, true);
        List<ProductEntity> products = productsPage.getContent();
        log.info(products);
        if(products.size()==0&&page!=0){
            return ResponseEntity.ok().body(Map.of("success", true, "message", "/?page=" + 0 + "&productName=" + productName));

        }
        response.put("products", products);
        response.put("currentPage", page);
        response.put("totalPages", productsPage.getTotalPages());

        long count = productService.countByStatusAndNameContainingIgnoreCase(true,productName);
        int firstNum=(size * page + 1);
        int secondNum=(products.size() + (size * page));
        if(firstNum>secondNum){
            firstNum=secondNum;
        }
        String panelCount = "Показано " + firstNum  + "-" +secondNum  + " из " + count;
        log.info(panelCount);
        response.put("panelCount", panelCount);

        response.put("productName", productName);
        return ResponseEntity.ok().body(response);
    }


}
