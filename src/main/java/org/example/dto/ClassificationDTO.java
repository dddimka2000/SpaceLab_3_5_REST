package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;


@Data
public class ClassificationDTO {
    @Size(min = 2, max = 30, message = "Название должно содержать от 2 до 30 символов.")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я_ ]+$", message = "Название должно содержать только буквы a-z A-Z а-я А-Я, цифры 0-9, символ подчеркивания \"_\" и пробел.")
    public String name;
    @NotNull(message = "Пожалуйста, выберите статус.")
    private Boolean status;
    private MultipartFile file;
    @Size(min = 2, max = 450, message = "Описание должно содержать от 2 до 450 символов.")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я_ ,=.!?]+$", message = "Описание должно содержать только буквы a-z A-Z а-я А-Я, цифры 0-9, символы \",!.?=_\" и пробел.")
    public String description;
}
