package org.example.controller.adminController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.example.dto.ClassificationDTO;
import org.example.entity.CategoryEntity;
import org.example.entity.ClassificationEntity;
import org.example.service.CategoryService;
import org.example.service.ClassificationService;
import org.example.util.ClassificationValidatorAndConvert.ClassificationDTOConvert;
import org.example.util.ClassificationValidatorAndConvert.ClassificationValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/admin/classifications")
@Tag(name = "Classification Admin", description = "описание, бла-бла-бла")
public class ClassificationAdminController {
    String OldName = null;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final
    CategoryService categoryService;

    @Value("${spring.regex}")
    String regex;
    @Value("${spring.pathImg}")
    String path;
    private final
    ClassificationValidator classificationValidator;

    private final
    ClassificationService classificationService;

    public ClassificationAdminController(ClassificationValidator classificationValidator, ClassificationService classificationService, CategoryService categoryService) {
        this.classificationValidator = classificationValidator;
        this.classificationService = classificationService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> showClassificationsAdminPage() {
        Map<String, Object> response = new HashMap<>();
        response.put("classifications", classificationService.findAllClassificationEntities());
        return ResponseEntity.ok().body(response);
    }


    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createClassificationAdminPagePost(@ModelAttribute("classificationDTO") @Valid ClassificationDTO classificationDTO, BindingResult bindingResult) {
        log.info("ClassificationController-createClassificationAdminPagePost start");
        Map<String, Object> response = new HashMap<>();
        if (classificationDTO.getFile() == null && classificationDTO.getFile().isEmpty()) {
            bindingResult.rejectValue("file", "", "Пожалуйста, выберите файл для загрузки.");
        }
        classificationValidator.validate(classificationDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        log.warn(classificationDTO);
        ClassificationDTOConvert classificationDTOConvert = new ClassificationDTOConvert();
        ClassificationEntity classificationEntity = classificationDTOConvert.convertToEntity(classificationDTO, regex, path, new ClassificationEntity());
        classificationService.save(classificationEntity);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "createClassification successfully"));
    }

    public static void putErrors(BindingResult bindingResult, Map<String, Object> response, Logger log) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
        response.put("fields", fieldErrors);
    }


    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> editClassificationAdminPageShow(@PathVariable String name) {
        ClassificationDTO classificationDTO=new ClassificationDTO();
        log.info("wwwwwwwwwwwwwwwwwwwwwww" + contextPath);
        Map<String, Object> response = new HashMap<>();
        OldName = name;
        response.put("oldName", OldName);
        Optional<ClassificationEntity> classificationEntity = classificationService.findByName(OldName);
        if (classificationEntity.isPresent()) {
            classificationDTO.setName(classificationEntity.get().getName());
            classificationDTO.setDescription(classificationEntity.get().getDescription());
            classificationDTO.setStatus(classificationEntity.get().getStatus());
            response.put("path", classificationEntity.get().getPath());
            List<CategoryEntity> categoryEntityList = categoryService.findAllCategoriesByClassificationEntity(classificationEntity.get());
            response.put("categories", categoryEntityList);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/{name}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> editClassificationAdminPagePost(@PathVariable String name, @ModelAttribute("classificationDTO") @Valid ClassificationDTO classificationDTO, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        List<CategoryEntity> categoryEntityList = categoryService.findAllCategoriesByClassificationEntity(classificationService.findByName(OldName).get());
        response.put("categories", categoryEntityList);

        log.info("ClassificationController-editClassificationAdminPagePost start");
        classificationValidator.validate(classificationDTO, bindingResult, OldName);

        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
            response.put("fields", fieldErrors);
            return ResponseEntity.badRequest().body(response);
        }
        log.warn(classificationDTO);
        ClassificationDTOConvert classificationDTOConvert = new ClassificationDTOConvert();

        ClassificationEntity classificationEntity = classificationService.findByName(OldName).get();
        classificationEntity = classificationDTOConvert.convertToEntity(classificationDTO, regex, path, classificationEntity);
        classificationService.save(classificationEntity);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Classification edit successfully"));
    }

    @DeleteMapping("/{nameClassification}/delete/{nameCategory}")
    public ResponseEntity<String> deleteCategory(@PathVariable String nameClassification, @PathVariable String nameCategory) {
        log.info("DeleteMapping" + nameClassification + "start");
        Optional<CategoryEntity> categoryEntity = categoryService.findByName(nameCategory);
        if (categoryEntity.isPresent()) {
            categoryService.delete(categoryEntity.get());
            log.info("Category deleted successfully");
            return ResponseEntity.ok().body("Category deleted successfully");
        } else {
            log.error("Category deleted unsuccessfully");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{nameClassification}/getElementsTable")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getElementsTable(@PathVariable String nameClassification) {
        Map<String, Object> response = new HashMap<>();
        List<CategoryEntity> categoryEntityList = categoryService.findAllCategoriesByClassificationEntity(classificationService.findByName(nameClassification).get());
        response.put("categoryEntityList", categoryEntityList);
        return ResponseEntity.ok().body(response);
    }
}
