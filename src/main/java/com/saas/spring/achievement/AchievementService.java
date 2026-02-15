package com.saas.spring.achievement;

import com.saas.spring.achievement.dto.AchievementInDto;
import com.saas.spring.achievement.dto.AchievementOutDto;
import com.saas.spring.achievement.dto.AchievementUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository){
        this.achievementRepository = achievementRepository;
    }

    private AchievementOutDto convertToOutDto(Achievement achievement){
            return new AchievementOutDto(
                    achievement.getId(),
                    achievement.getName()
            );
    }

    private Achievement findAchievementById(Long id) {
        return this.achievementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Achievement no encontrado con id: " + id));
    }

    public List<AchievementOutDto> getAllAchievement() {
        return this.achievementRepository.findAll()
                .stream()
                .map(this::convertToOutDto)
                .toList();
    }

    public AchievementOutDto getById(Long id){
        return this.achievementRepository.findById(id)
                .map(this::convertToOutDto)
                .orElseThrow(() -> new IllegalArgumentException("Achievement no encontrado con id: " + id));
    }

    public AchievementOutDto createAchievement(AchievementInDto dto) {
        var newAchievement = this.achievementRepository.save(
                Achievement.builder()
                        .name(dto.name())
                        .build()
        );
        return this.convertToOutDto(newAchievement);
    }

    public AchievementOutDto updateAchievement(AchievementUpdateDto dto, Long id) {
        Achievement achievement = this.findAchievementById(id);

        achievement.setName(
                dto.name() != null ? dto.name() : achievement.getName()
        );

        return this.convertToOutDto(achievement);
    }

    public void deleteAchievement(Long id) {
        Achievement achievement = this.findAchievementById(id);
        this.achievementRepository.delete(achievement);
    }
}
