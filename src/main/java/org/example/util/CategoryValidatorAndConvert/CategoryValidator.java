package org.example.util.CategoryValidatorAndConvert;

import lombok.extern.log4j.Log4j2;
import org.example.dto.CategoryDTO;
import org.example.dto.ClassificationDTO;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Log4j2
public class CategoryValidator implements Validator {
    private final
    CategoryService categoryService;

    public CategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryService.class.equals(clazz);
    }

    private final long maxFileSize = 5 * 1024 * 1024; // Максимальный размер файла (5 МБ)
    private final List<String> supportedImageFormats = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif");

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDTO categoryDTO = (CategoryDTO) target;
        MultipartFile multipartFile = categoryDTO.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        Optional<CategoryEntity> categoryEntity = categoryService.findByName(categoryDTO.getName());
        if (categoryEntity.isPresent()) {
            errors.rejectValue("name", "", "Категория с таким именем уже существует");
        }
    }

    public void validate(Object target, Errors errors, String name) {
        CategoryDTO categoryDTO = (CategoryDTO) target;
        MultipartFile multipartFile = categoryDTO.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        log.info(categoryDTO.getName());
        log.info(name);
        log.info(categoryDTO.getName().equals(name));
        if (!categoryDTO.getName().equals(name)) {
            Optional<CategoryEntity> categoryEntity = categoryService.findByName(categoryDTO.getName());
            if (categoryEntity.isPresent()) {
                errors.rejectValue("name", "", "Категория с таким именем уже существует");
            }
        }
    }
}
