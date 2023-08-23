package org.example.util.ClassificationValidatorAndConvert;

import org.example.dto.ClassificationDTO;
import org.example.entity.ClassificationEntity;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ClassificationDTOConvert {


    public ClassificationEntity convertToEntity(ClassificationDTO classificationDTO, String regex, String path,ClassificationEntity classificationEntity) {
        classificationEntity.setName(classificationDTO.getName());
        classificationEntity.setDescription(classificationDTO.getDescription());
        classificationEntity.setStatus(classificationDTO.getStatus());
        if (classificationDTO.getFile() != null && !classificationDTO.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + classificationDTO.getFile().getOriginalFilename();
            try {
                classificationDTO.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String mainImaginePath = regex + resultFilename;
            classificationEntity.setPath(mainImaginePath);
        }
        return classificationEntity;
    }
}
