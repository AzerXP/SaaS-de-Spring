package com.saas.spring.lesson;

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

import com.saas.spring.lesson.dto.LessonInDto;
import com.saas.spring.lesson.dto.LessonOutDto;
import com.saas.spring.lesson.dto.LessonUpdateDto;

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
@RequestMapping("/lessons")
@Slf4j
@Tag(name = "Lessons", description = "Gestión de lecciones del sistema")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    @Operation(summary = "Get all lessons", description = "Obtiene todas las lecciones disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lecciones obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = LessonOutDto.class)))
    })
    public ResponseEntity<List<LessonOutDto>> getLessons() {
        log.info("Obteniendo todas las lecciones");
        List<LessonOutDto> lessons = this.lessonService.getAllLessons();
        log.debug("Cantidad de lecciones encontradas: {}", lessons.size());
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lesson by ID", description = "Obtiene una lección específica por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lección encontrada",
            content = @Content(schema = @Schema(implementation = LessonOutDto.class),
                examples = {
                    @ExampleObject(name = "Lesson Example", summary = "Ejemplo de lección",
                        value = """
                        {
                          "id": 1,
                          "title": "Introducción a Java",
                          "description": "Conceptos básicos del lenguaje Java"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "404", description = "Lección no encontrada")
    })
    public ResponseEntity<LessonOutDto> getLesson(@PathVariable Long id) {
        log.info("Obteniendo lección con id: {}", id);
        LessonOutDto lesson = this.lessonService.getById(id);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    @Operation(summary = "Create lesson", description = "Crea una nueva lección en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lección creada exitosamente",
            content = @Content(schema = @Schema(implementation = LessonOutDto.class),
                examples = {
                    @ExampleObject(name = "Lesson Created", summary = "Lección creada",
                        value = """
                        {
                          "id": 1,
                          "title": "Introducción a Python",
                          "description": "Fundamentos del lenguaje Python"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<LessonOutDto> createLesson(@RequestBody @Valid LessonInDto dto) {
        log.info("Creando lección: {}", dto.title());
        LessonOutDto created = this.lessonService.createLesson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update lesson", description = "Actualiza parcialmente una lección existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lección actualizada",
            content = @Content(schema = @Schema(implementation = LessonOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Lección no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<LessonOutDto> updateLesson(
            @PathVariable Long id,
            @RequestBody @Valid LessonUpdateDto dto
    ) {
        log.info("Actualizando lección con id: {}", id);
        LessonOutDto updated = this.lessonService.updateLesson(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lesson", description = "Elimina permanentemente una lección y todas sus preguntas asociadas")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Lección y sus preguntas eliminadas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lección no encontrada")
    })
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        log.info("Borrando lección con id: {}", id);
        this.lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
