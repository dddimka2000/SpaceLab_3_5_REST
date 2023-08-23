package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
@Data
public class UploadImg {
    @NotNull(message = "Пожалуйста, выберите файл для загрузки.")
    private MultipartFile file;

}
