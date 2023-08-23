package org.example.util.UserValidatorAndConvert;

import org.example.dto.UploadImg;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class PhotoValidator implements Validator {
    private final List<String> supportedImageFormats = Arrays.asList("image/jpeg", "image/png","image/jpg", "image/gif");

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
    private final long maxFileSize = 5 * 1024 * 1024; // Максимальный размер файла (5 МБ)

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile multipartFile= ((UploadImg) target).getFile();
        if(!supportedImageFormats.contains(multipartFile.getContentType())){
            errors.rejectValue("file", "image.format.invalid", "Неподдерживаемый формат изображения. Пожалуйста, выберите JPEG,PNG,JPG,GIF.");
        }
        if (multipartFile.getSize() > maxFileSize) {
            errors.rejectValue("file", "image.size.invalid", "Файл не должен превышать 5 МБ.");
        }
    }
}
