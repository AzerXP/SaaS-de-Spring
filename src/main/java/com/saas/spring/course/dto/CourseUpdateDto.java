package com.saas.spring.course.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record CourseUpdateDto(
        @Nullable
        @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
        String title,

        @Nullable
        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String description
) {
}
