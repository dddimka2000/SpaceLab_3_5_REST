package org.example.util.ClassificationValidatorAndConvert;

import org.example.dto.ClassificationDTO;
import org.example.dto.UploadImg;
import org.example.dto.UserDTO;
import org.example.entity.ClassificationEntity;
import org.example.entity.UserEntity;
import org.example.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component

public class ClassificationValidator implements Validator {
    private final
    ClassificationService classificationService;

    public ClassificationValidator(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ClassificationDTO.class.equals(clazz);
    }

    private final long maxFileSize = 5 * 1024 * 1024; // Максимальный размер файла (5 МБ)
    private final List<String> supportedImageFormats = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif");

    @Override
    public void validate(Object target, Errors errors) {
        ClassificationDTO classificationDTO = (ClassificationDTO) target;
        MultipartFile multipartFile = classificationDTO.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        Optional<ClassificationEntity> classificationEntity = classificationService.findByName(classificationDTO.getName());
        if (classificationEntity.isPresent()) {
            errors.rejectValue("name", "", "Классификация с таким именем уже существует");
        }
    }

    public void validate(Object target, Errors errors, String name) {
        ClassificationDTO classificationDTO = (ClassificationDTO) target;
        MultipartFile multipartFile = classificationDTO.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (!supportedImageFormats.contains(multipartFile.getContentType())) {
                errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
            }
            if (multipartFile.getSize() > maxFileSize) {
                errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
            }
        }
        if (!classificationDTO.getName().equals(name)) {
            Optional<ClassificationEntity> classificationEntity = classificationService.findByName(classificationDTO.getName());
            if (classificationEntity.isPresent()) {
                errors.rejectValue("name", "", "Классификация с таким именем уже существует");
            }
        }
    }
}
