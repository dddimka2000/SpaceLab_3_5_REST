package org.example.util;

import org.example.dto.CategoryDTO;
import org.example.dto.ProductDto;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.entity.ProductEntity;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Component
public class ProductValidator implements Validator {
    private final ProductService productService;

    public ProductValidator(ProductService productService, ClassificationService classificationService, CategoryService categoryService) {
        this.productService = productService;
        this.classificationService = classificationService;
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
        ProductDto productDto = (ProductDto) target;
        MultipartFile multipartFile = productDto.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        Optional<ProductEntity> productEntity = productService.findByName(productDto.getName());
        if (productEntity.isPresent()) {
            errors.rejectValue("name", "", "Продукт с таким именем уже существует");
        }
    }
    public void validate(Object target, Errors errors, String name) {
        ProductDto productDto = (ProductDto) target;
        MultipartFile multipartFile = productDto.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        if (!productDto.getName().equals(name)) {
            Optional<ProductEntity> productEntity = productService.findByName(productDto.getName());
            if (productEntity.isPresent()) {
                errors.rejectValue("name", "", "Продукт с таким именем уже существует");
            }
        }
    }
    final
    ClassificationService classificationService;
    final
    CategoryService categoryService;
    public void validate(Object target, Errors errors,String nameClassification, String nameCategory) {
        ProductDto productDto = (ProductDto) target;
        MultipartFile multipartFile = productDto.getFile();
        Optional<CategoryEntity> categoryEntity=categoryService.findByName(nameCategory);
        Optional<ClassificationEntity> classificationEntity=classificationService.findByName(nameClassification);
        if(!classificationEntity.isPresent()){
            errors.rejectValue("selectedClassification", "", "Попытка внести несуществующую классификацию");
        }

        if(!categoryEntity.isPresent()){
            errors.rejectValue("selectedCategory","","Попытка внести несуществующую категорию");
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        Optional<ProductEntity> productEntity = productService.findByName(productDto.getName());
        if (productEntity.isPresent()) {
            errors.rejectValue("name", "", "Продукт с таким именем уже существует");
        }
    }


    public void validate(Object target, Errors errors,String nameClassification, String nameCategory,String name) {
        ProductDto productDto = (ProductDto) target;
        MultipartFile multipartFile = productDto.getFile();
        Optional<CategoryEntity> categoryEntity=categoryService.findByName(nameCategory);
        Optional<ClassificationEntity> classificationEntity=classificationService.findByName(nameClassification);
        if(!classificationEntity.isPresent()){
            errors.rejectValue("selectedClassification", "", "Попытка внести несуществующую классификацию");
        }

        if(!categoryEntity.isPresent()){
            errors.rejectValue("selectedCategory","","Попытка внести несуществующую категорию");
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        Optional<ProductEntity> productEntity = productService.findByName(productDto.getName());
        if (productEntity.isPresent() && productEntity.get().getName()!=name) {
            errors.rejectValue("name", "", "Продукт с таким именем уже существует");
        }
    }

}
