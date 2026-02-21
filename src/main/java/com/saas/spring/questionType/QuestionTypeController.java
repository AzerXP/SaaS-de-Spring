package com.saas.spring.questionType;

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

import com.saas.spring.questionType.dto.QuestionTypeInDto;
import com.saas.spring.questionType.dto.QuestionTypeOutDto;
import com.saas.spring.questionType.dto.QuestionTypeUpdateDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/question_types")
@Slf4j
public class QuestionTypeController {

    private QuestionTypeService questionTypeService;

    public QuestionTypeController(QuestionTypeService questionTypeService){
        this.questionTypeService = questionTypeService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionTypeOutDto>> getQuestionTypes(){
        log.info("Obteniendo todos los tipos de preguntas");
        var questions = this.questionTypeService.getAllQuestionTypes();
        log.info("Cantidad de tipos de preguntas obtenidas: {}", questions.size());
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionTypeOutDto> getQuestionType(
            @PathVariable Long id
    ){
        log.info("Obteniendo tipo de pregunta con id: {}", id);
        var question = this.questionTypeService.getById(id);
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<QuestionTypeOutDto> createQuestionType(
            @RequestBody QuestionTypeInDto dto
    ){
        log.info("Creando tipo de pregunta: {}", dto.name());
        var created = questionTypeService.createQuestionType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QuestionTypeOutDto> updateQuestionType(
            @PathVariable Long id,
            @RequestBody QuestionTypeUpdateDto dto
    ){
        log.info("Actualizando tipo de pregunta con id: {}", id);
        var updated = questionTypeService.updateQuestionType(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestioType(
            @PathVariable Long id
    ){
        log.info("Borrando tipo de pregunta con id: {}", id);
        this.questionTypeService.deleteQuestionType(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
