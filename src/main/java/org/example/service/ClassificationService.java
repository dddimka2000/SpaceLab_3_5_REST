package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.ClassificationEntity;
import org.example.entity.UserEntity;
import org.example.repository.ClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ClassificationService {
    private final
    ClassificationRepository classificationRepository;

    public ClassificationService(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    public Optional<ClassificationEntity> findByName(String login) {
        log.info("ClassificationService-findByName start: " + login);
        Optional<ClassificationEntity> classificationEntity = classificationRepository.findByName(login);
        if (classificationEntity.isPresent()) {
            log.info("ClassificationService-findByName successful: " + classificationEntity);
        } else {
            log.info("classificationEntity empty");
        }
        return classificationEntity;
    }

    public void save(ClassificationEntity classificationEntity) {
        log.info("ClassificationEntity-save start: " + classificationEntity.getId());
        classificationRepository.save(classificationEntity);
        log.info("ClassificationEntity-save successful");
    }

    public List<ClassificationEntity> findAllClassificationEntities() {
        log.info("ClassificationService-findAllClassificationEntities");
        List<ClassificationEntity> classificationEntities = classificationRepository.findAll();
        log.info("ClassificationService-findAllClassificationEntities successful");
        return classificationEntities;
    }
    public void delete(ClassificationEntity classificationEntity) {
        log.info("ClassificationEntity-delete start: " + classificationEntity.getId());
        classificationRepository.delete(classificationEntity);
        log.info("ClassificationEntity-delete successful");
    }



}
