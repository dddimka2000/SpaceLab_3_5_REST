package org.example.util;

import lombok.extern.log4j.Log4j2;
import org.example.dto.OrderTableDTO;
import org.example.dto.UserDTO;
import org.example.entity.ProductEntity;
import org.example.service.ProductService;
import org.example.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Log4j2
public class OrderValidator implements Validator {
    public OrderValidator(UserEntityService userEntityService, ProductService productService) {
        this.userEntityService = userEntityService;
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
         return OrderTableDTO.class.equals(clazz);
    }

    final
    UserEntityService userEntityService;
    final
    ProductService productService;
    @Override
    public void validate(Object target, Errors errors) {
        OrderTableDTO orderTableDTO=(OrderTableDTO) target;
        if(!userEntityService.findByLogin(orderTableDTO.getLogin()).isPresent()){
            errors.rejectValue("login","", "Пользователя с данным логином не существует");
        }
        AtomicBoolean findProducts=new AtomicBoolean(false);
        orderTableDTO.getProducts().forEach(productName->{
            Optional<ProductEntity> productEntity=productService.findByName(productName);
            if(!productEntity.isPresent()){
                log.warn(productName+" " +productEntity);
                findProducts.set(true);
            }
        });
        if(findProducts.get()==true){
            errors.rejectValue("products","", "Такого продукта не существует");
        }
        AtomicBoolean validQuantity=new AtomicBoolean(false);
        orderTableDTO.getQuantity().forEach(quantity->{
            try {
                Integer.parseInt(quantity);
                if (quantity.length() > 6) {
                    throw new NumberFormatException("Слишком большое число");
                }
            } catch (NumberFormatException e) {
                validQuantity.set(true);
            }
        });
        if(validQuantity.get()==true){
            errors.rejectValue("quantity","", "Пожалуйста, введите допустимое количество. Например, 123456, но не более шести цифр.");
        }
    }
}
