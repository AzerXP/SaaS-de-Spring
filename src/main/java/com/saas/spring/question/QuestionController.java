package com.saas.spring.question;

import com.saas.spring.question.dto.QuestionInDto;
import com.saas.spring.question.dto.QuestionOutDto;
import com.saas.spring.question.dto.QuestionUpdateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@Slf4j
@Tag(name = "Questions", description = "Gestión de preguntas del sistema")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @GetMapping
    @Operation(summary = "Get all questions", description = "Obtiene todas las preguntas registradas en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Preguntas obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionOutDto.class)))
    })
    public ResponseEntity<List<QuestionOutDto>> getQuestions(){
        log.info("Obteniendo todas las preguntas");
        var questions = this.questionService.getAllQuestions();
        log.info("Cantidad de preguntas obtenidas: {}", questions.size());
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID", description = "Obtiene una pregunta específica por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pregunta encontrada",
            content = @Content(schema = @Schema(implementation = QuestionOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
    })
    public ResponseEntity<QuestionOutDto> getQuestion(
            @PathVariable Long id
    ){
        log.info("Obteniendo pregunta con id: {}", id);
        var question = this.questionService.getById(id);
        return ResponseEntity.ok(question);
    }

    @PostMapping
    @Operation(summary = "Create question", description = "Crea una nueva pregunta asociada a un tipo de pregunta existente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pregunta creada exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionOutDto.class),
                examples = {
                    @ExampleObject(name = "Multiple Choice Question", summary = "Pregunta de selección múltiple",
                        value = """
                        {
                          "id": 1,
                          "text": "¿Cuál de los siguientes es un lenguaje orientado a objetos?",
                          "questionTypeId": 1
                        }
                        """),
                    @ExampleObject(name = "Blank Spaces Question", summary = "Pregunta de espacios en blanco",
                        value = """
                        {
                          "id": 2,
                          "text": "El perro ___ rápidamente por el parque.",
                          "questionTypeId": 2
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Tipo de pregunta no encontrado")
    })
    public ResponseEntity<QuestionOutDto> createQuestion(
            @Valid @RequestBody QuestionInDto dto
    ){
        log.info("Creando pregunta: {}", dto.text());
        var created = questionService.createQuestion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update question", description = "Actualiza parcialmente una pregunta existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pregunta actualizada",
            content = @Content(schema = @Schema(implementation = QuestionOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Pregunta o tipo de pregunta no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<QuestionOutDto> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionUpdateDto dto
    ){
        log.info("Actualizando pregunta con id: {}", id);
        var updated = questionService.updateQuestion(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question", description = "Elimina permanentemente una pregunta del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pregunta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
    })
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long id
    ){
        log.info("Borrando pregunta con id: {}", id);
        this.questionService.deleteQuestion(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
