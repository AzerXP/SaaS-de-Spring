package com.saas.spring.question;

import com.saas.spring.question.dto.QuestionInDto;
import com.saas.spring.question.dto.QuestionOutDto;
import com.saas.spring.question.dto.QuestionUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionOutDto>> getQuestions(){
        log.info("Obteniendo todas las preguntas");
        var questions = this.questionService.getAllQuestions();
        log.info("Cantidad de preguntas obtenidas: {}", questions.size());
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionOutDto> getQuestion(
            @PathVariable Long id
    ){
        log.info("Obteniendo pregunta con id: {}", id);
        var question = this.questionService.getById(id);
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<QuestionOutDto> createQuestion(
            @RequestBody QuestionInDto dto
    ){
        log.info("Creando pregunta: {}", dto.text());
        var created = questionService.createQuestion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QuestionOutDto> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionUpdateDto dto
    ){
        log.info("Actualizando pregunta con id: {}", id);
        var updated = questionService.updateQuestion(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long id
    ){
        log.info("Borrando pregunta con id: {}", id);
        this.questionService.deleteQuestion(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
