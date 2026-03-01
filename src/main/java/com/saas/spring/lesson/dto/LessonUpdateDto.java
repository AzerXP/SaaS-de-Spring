package com.saas.spring.lesson.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record LessonUpdateDto(
        @Nullable
        @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
        String title,

        @Nullable
        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String description
) {
}
