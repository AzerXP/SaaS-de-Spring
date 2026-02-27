package com.saas.spring.questionConfig;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saas.spring.questionConfig.dto.QuestionConfigInDto;
import com.saas.spring.questionConfig.dto.QuestionConfigOutDto;
import com.saas.spring.questionConfig.dto.QuestionConfigUpdateDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/question_configs")
@Slf4j
public class QuestionConfigController {

    private final QuestionConfigService questionConfigService;

    public QuestionConfigController(QuestionConfigService questionConfigService){
        this.questionConfigService = questionConfigService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionConfigOutDto>> getQuestionConfigs(){
        log.info("Obteniendo todas las configuraciones de preguntas");
        var questionConfigs = this.questionConfigService.getAll();
        log.info("Cantidad de configuraciones de preguntas obtenidas: {}", questionConfigs.size());
        return ResponseEntity.ok(questionConfigs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionConfigOutDto> getQuestionConfig(
            @PathVariable Long id
    ){
        log.info("Obteniendo la configuracion de pregunta con id: {}", id);
        var questionConfig = this.questionConfigService.getById(id);
        return ResponseEntity.ok(questionConfig);
    }

    @PostMapping
    public ResponseEntity<QuestionConfigOutDto> createQuestionConfig(
            @Valid @RequestBody QuestionConfigInDto dto
    ){
        log.info("Creando la configuracion de pregunta: {}", dto.config());
        var created = questionConfigService.createQuestionConfig(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QuestionConfigOutDto> updateQuestionConfig(
            @PathVariable Long id,
            @Valid @RequestBody QuestionConfigUpdateDto dto
    ){
        log.info("Actualizando la configuracion de pregunta con id: {}", id);
        var updated = questionConfigService.updateQuestionConfig(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionConfig(
            @PathVariable Long id
    ){
        log.info("Borrando la configuracion de pregunta con id: {}", id);
        this.questionConfigService.deleteQuestionConfig(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
