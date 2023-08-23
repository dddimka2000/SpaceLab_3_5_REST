package org.example.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductWithChoiceDto {
    Integer id;
    @Size(min = 2, max = 30, message = "Название должно содержать от 2 до 30 символов.")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я_ ]+$", message = "Название должно содержать только буквы a-z A-Z а-я А-Я, цифры 0-9, символ подчеркивания \"_\" и пробел.")
    public String name;

    private String path;

    private MultipartFile file;

    @NotNull(message = "Пожалуйста, выберите статус.")
    private Boolean status;
    @Size(min = 2, max = 450, message = "Описание должно содержать от 2 до 450 символов.")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я_ ,=.!?]+$", message = "Описание должно содержать только буквы a-z A-Z а-я А-Я, цифры 0-9, символы \",!.?=_\" и пробел.")
    public String description;

    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0.")
    @Digits(integer = 7, fraction = 2, message = "Цена должна быть адекватной, до 7 цифр.")
    public BigDecimal price;

    String nameClassification;
    String nameCategory;

}
