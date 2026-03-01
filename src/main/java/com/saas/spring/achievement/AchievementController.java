package com.saas.spring.achievement;

import com.saas.spring.achievement.dto.AchievementInDto;
import com.saas.spring.achievement.dto.AchievementOutDto;
import com.saas.spring.achievement.dto.AchievementUpdateDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/achievements")
@Slf4j
@Tag(name = "Achievements", description = "Gestión de logros del sistema")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    @Operation(summary = "Get all achievements", description = "Obtiene todos los logros disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logros obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = AchievementOutDto.class)))
    })
    public ResponseEntity<List<AchievementOutDto>> getAchievements(){
        log.info("Obteniendo todos los logros");
        List<AchievementOutDto> achievements = this.achievementService.getAllAchievement();
        log.debug("Cantidad de logros encontrados: {}", achievements.size());
        return ResponseEntity.ok(achievements);
    }

    @PostMapping
    @Operation(summary = "Create achievement", description = "Crea un nuevo logro en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Logro creado exitosamente",
            content = @Content(schema = @Schema(implementation = AchievementOutDto.class),
                examples = {
                    @ExampleObject(name = "Achievement Example", summary = "Ejemplo de logro creado",
                        value = """
                        {
                          "id": 1,
                          "name": "Primeros Pasos"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<AchievementOutDto> createAchievement(
            @RequestBody @Valid AchievementInDto dto
    ){
        log.info("Creando logro: {}", dto.name());
        var created = this.achievementService.createAchievement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get achievement by ID", description = "Obtiene un logro específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logro encontrado",
            content = @Content(schema = @Schema(implementation = AchievementOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    public ResponseEntity<AchievementOutDto> getAchievement(
            @PathVariable Long id
    ){
        log.info("Obteniendo logro con id: {}", id);
        AchievementOutDto achievement = this.achievementService.getById(id);
        return ResponseEntity.ok(achievement);
    }

    @PatchMapping(path = "/{id}")
    @Operation(summary = "Update achievement", description = "Actualiza parcialmente un logro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logro actualizado",
            content = @Content(schema = @Schema(implementation = AchievementOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Logro no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<AchievementOutDto> updateAchievement(
            @PathVariable Long id,
            @RequestBody @Valid AchievementUpdateDto dto
    ){
        log.info("Actualizando logro con id: {}", id);
        AchievementOutDto updated = this.achievementService.updateAchievement(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete achievement", description = "Elimina permanentemente un logro del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Logro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    })
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable Long id
    ){
        log.info("Borrando logro con id {}", id);
        this.achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
