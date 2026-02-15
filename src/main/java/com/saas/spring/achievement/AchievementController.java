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

@RestController
@RequestMapping("/achievements")
@Slf4j
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<List<AchievementOutDto>> getAchievements(){
        log.info("Obteniendo todos los logros");
        List<AchievementOutDto> achievements = this.achievementService.getAllAchievement();
        log.debug("Cantidad de logros encontrados: {}", achievements.size());
        return ResponseEntity.ok(achievements);
    }

    @PostMapping
    public ResponseEntity<AchievementOutDto> createAchievement(
            @RequestBody @Valid AchievementInDto dto
    ){
        log.info("Creando logro: {}", dto.name());
        var created = this.achievementService.createAchievement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AchievementOutDto> getAchievement(
            @PathVariable Long id
    ){
        log.info("Obteniendo logro con id: {}", id);
        AchievementOutDto achievement = this.achievementService.getById(id);
        return ResponseEntity.ok(achievement);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<AchievementOutDto> updateAchievement(
            @PathVariable Long id,
            @RequestBody @Valid AchievementUpdateDto dto
    ){
        log.info("Actualizando logro con id: {}", id);
        AchievementOutDto updated = this.achievementService.updateAchievement(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
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
