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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/question_configs")
@Slf4j
@Tag(name = "Question Configs", description = "Gestión de configuraciones de preguntas (valida contra schema del tipo)")
public class QuestionConfigController {

    private final QuestionConfigService questionConfigService;

    public QuestionConfigController(QuestionConfigService questionConfigService){
        this.questionConfigService = questionConfigService;
    }

    @GetMapping
    @Operation(summary = "Get all question configs", description = "Obtiene todas las configuraciones de preguntas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuraciones obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionConfigOutDto.class)))
    })
    public ResponseEntity<List<QuestionConfigOutDto>> getQuestionConfigs(){
        log.info("Obteniendo todas las configuraciones de preguntas");
        var questionConfigs = this.questionConfigService.getAll();
        log.info("Cantidad de configuraciones de preguntas obtenidas: {}", questionConfigs.size());
        return ResponseEntity.ok(questionConfigs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question config by ID", description = "Obtiene una configuración específica por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuración encontrada",
            content = @Content(schema = @Schema(implementation = QuestionConfigOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Configuración no encontrada")
    })
    public ResponseEntity<QuestionConfigOutDto> getQuestionConfig(
            @PathVariable Long id
    ){
        log.info("Obteniendo la configuracion de pregunta con id: {}", id);
        var questionConfig = this.questionConfigService.getById(id);
        return ResponseEntity.ok(questionConfig);
    }

    @PostMapping
    @Operation(summary = "Create question config", description = "Crea una configuración para una pregunta. El config debe validar contra el schema del QuestionType asociado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Configuración creada exitosamente",
            content = @Content(schema = @Schema(implementation = QuestionConfigOutDto.class),
                examples = {
                    @ExampleObject(name = "MULTIPLE_CHOICE Config", summary = "Configuración para selección múltiple",
                        value = """
                        {
                          "questionId": 1,
                          "config": {
                            "options": [
                              {"id": 1, "text": "Java", "is_correct": false},
                              {"id": 2, "text": "Python", "is_correct": true},
                              {"id": 3, "text": "JavaScript", "is_correct": false}
                            ],
                            "max_selection": 1,
                            "shuffle_options": true
                          }
                        }
                        """),
                    @ExampleObject(name = "BLANK_SPACES Config", summary = "Configuración para espacios en blanco",
                        value = """
                        {
                          "questionId": 2,
                          "config": {
                            "text": "El perro ___ rápidamente por el parque.",
                            "blanks": [
                              {
                                "placeholder": "blank1",
                                "correct_answers": ["corre", "corriendo"],
                                "case_sensitive": false
                              }
                            ]
                          }
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "El config no cumple con el schema del QuestionType o datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
    })
    public ResponseEntity<QuestionConfigOutDto> createQuestionConfig(
            @Valid @RequestBody QuestionConfigInDto dto
    ){
        log.info("Creando la configuracion de pregunta: {}", dto.config());
        var created = questionConfigService.createQuestionConfig(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update question config", description = "Actualiza parcialmente una configuración. El nuevo config valida contra el schema del QuestionType")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuración actualizada",
            content = @Content(schema = @Schema(implementation = QuestionConfigOutDto.class))),
        @ApiResponse(responseCode = "400", description = "El config no cumple con el schema o datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Configuración o pregunta no encontrada")
    })
    public ResponseEntity<QuestionConfigOutDto> updateQuestionConfig(
            @PathVariable Long id,
            @Valid @RequestBody QuestionConfigUpdateDto dto
    ){
        log.info("Actualizando la configuracion de pregunta con id: {}", id);
        var updated = questionConfigService.updateQuestionConfig(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question config", description = "Elimina permanentemente una configuración de pregunta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Configuración eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Configuración no encontrada")
    })
    public ResponseEntity<Void> deleteQuestionConfig(
            @PathVariable Long id
    ){
        log.info("Borrando la configuracion de pregunta con id: {}", id);
        this.questionConfigService.deleteQuestionConfig(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
