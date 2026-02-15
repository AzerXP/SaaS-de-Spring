package com.saas.spring.achievement.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record AchievementUpdateDto(
        @Nullable
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name
) {
}
