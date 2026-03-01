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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/question_types")
@Slf4j
@Tag(name = "Question Types", description = "Gestión de tipos de preguntas disponibles en el sistema")
public class QuestionTypeController {

    private QuestionTypeService questionTypeService;

    public QuestionTypeController(QuestionTypeService questionTypeService){
        this.questionTypeService = questionTypeService;
    }

    @GetMapping
    @Operation(summary = "Get all question types", description = "Obtiene todos los tipos de preguntas disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipos de preguntas obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionTypeOutDto.class)))
    })
    public ResponseEntity<List<QuestionTypeOutDto>> getQuestionTypes(){
        log.info("Obteniendo todos los tipos de preguntas");
        var questions = this.questionTypeService.getAllQuestionTypes();
        log.info("Cantidad de tipos de preguntas obtenidas: {}", questions.size());
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question type by ID", description = "Obtiene un tipo de pregunta específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de pregunta encontrado",
            content = @Content(schema = @Schema(implementation = QuestionTypeOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de pregunta no encontrado")
    })
    public ResponseEntity<QuestionTypeOutDto> getQuestionType(
            @PathVariable Long id
    ){
        log.info("Obteniendo tipo de pregunta con id: {}", id);
        var question = this.questionTypeService.getById(id);
        return ResponseEntity.ok(question);
    }

    @PostMapping
    @Operation(summary = "Create question type", description = "Crea un nuevo tipo de pregunta con su schema de configuración")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tipo de pregunta creado exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionTypeOutDto.class),
                examples = {
                    @ExampleObject(name = "MULTIPLE_CHOICE", summary = "Tipo selección múltiple",
                        value = """
                        {
                          "id": 1,
                          "name": "MULTIPLE_CHOICE",
                          "config_schema": {
                            "type": "object",
                            "required": ["options", "max_selection"],
                            "properties": {
                              "options": {
                                "type": "array",
                                "minItems": 2,
                                "items": {
                                  "type": "object",
                                  "required": ["id", "text", "is_correct"],
                                  "properties": {
                                    "id": { "type": "integer" },
                                    "text": { "type": "string" },
                                    "is_correct": { "type": "boolean" }
                                  }
                                }
                              },
                              "max_selection": { "type": "integer" },
                              "shuffle_options": { "type": "boolean" }
                            }
                          }
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<QuestionTypeOutDto> createQuestionType(
            @RequestBody QuestionTypeInDto dto
    ){
        log.info("Creando tipo de pregunta: {}", dto.name());
        var created = questionTypeService.createQuestionType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update question type", description = "Actualiza parcialmente un tipo de pregunta existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de pregunta actualizado",
            content = @Content(schema = @Schema(implementation = QuestionTypeOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de pregunta no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<QuestionTypeOutDto> updateQuestionType(
            @PathVariable Long id,
            @RequestBody QuestionTypeUpdateDto dto
    ){
        log.info("Actualizando tipo de pregunta con id: {}", id);
        var updated = questionTypeService.updateQuestionType(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question type", description = "Elimina permanentemente un tipo de pregunta del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tipo de pregunta eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de pregunta no encontrado")
    })
    public ResponseEntity<Void> deleteQuestioType(
            @PathVariable Long id
    ){
        log.info("Borrando tipo de pregunta con id: {}", id);
        this.questionTypeService.deleteQuestionType(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
